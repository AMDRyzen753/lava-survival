package link.therealdomm.heldix.lavasurvival.util.task;

import link.therealdomm.heldix.lavasurvival.util.lava.LavaAlgorithm;
import link.therealdomm.heldix.lavasurvival.util.location.SimpleLocation;
import link.therealdomm.heldix.lavasurvival.util.region.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class LocationIterator {

    public static LocationIterator get() {
        return new LocationIterator();
    }

    private ExecutorService executorService = Executors.newCachedThreadPool((Runnable r) -> {
        final Thread thread = new Thread(r, "Completable Location Iterator");
        thread.setDaemon(true);
        return thread;
    });

    public Future<List<SimpleLocation>> submitCalculation(LavaAlgorithm.CalculatorValues values) {
        CompletableFuture<List<SimpleLocation>> completableFuture = new CompletableFuture<>();
        this.executorService.submit(() -> {
            List<SimpleLocation> locations = new ArrayList<>();
            CuboidRegion region = values.getRegion();
            World world = Bukkit.getWorld(values.getWorld());
            int xI = values.getXInitial();
            int yI = values.getYInitial();
            int zI = values.getZInitial();
            int run = values.getIteration();
            for (int x = xI - run; x <= xI + run; x++) {
                for (int y = yI - run; y <= yI + run; y++) {
                    for (int z = zI - run; z <= zI + run; z++) {
                        if ((xI - x) * (xI - x) + (yI - y) * (yI - y) + (zI - z) * (zI - z) <= (run*run)) {
                            if (region.isInside(1, x, y, z)) {
                                SimpleLocation location = new SimpleLocation(world.getName(), x, y, z);
                                if (!values.getAlgorithm().getLavaLocations().contains(location)) {
                                    values.getAlgorithm().getLavaLocations().add(location);
                                    locations.add(location);
                                }
                            }
                        }
                    }
                }
            }
            completableFuture.complete(locations);
        });
        this.executorService.shutdown();
        return completableFuture;
    }

}
