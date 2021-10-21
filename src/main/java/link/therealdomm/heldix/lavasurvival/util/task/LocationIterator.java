package link.therealdomm.heldix.lavasurvival.util.task;

import link.therealdomm.heldix.lavasurvival.util.lava.LavaAlgorithm;
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

    public Future<List<Location>> submitCalculation(LavaAlgorithm.CalculatorValues values) {
        CompletableFuture<List<Location>> completableFuture = new CompletableFuture<>();
        this.executorService.submit(() -> {
            List<Location> locations = new ArrayList<>();
            CuboidRegion region = values.getRegion();
            World world = Bukkit.getWorld(values.getWorld());
            int x = values.getXInitial();
            int y = values.getYInitial();
            int z = values.getZInitial();
            int iteration = values.getIteration();
            Location xPositive = new Location(world, x, y, z).add((iteration+1), 0, 0);
            Location xNegative = new Location(world, x, y, z).subtract((iteration+1), 0, 0);
            Location yPositive = new Location(world, x, y, z).add(0, (iteration+1), 0);
            Location yNegative = new Location(world, x, y, z).subtract(0, (iteration+1), 0);
            Location zPositive = new Location(world, x, y, z).add(0, 0, (iteration+1));
            Location zNegative = new Location(world, x, y, z).subtract(0, 0, (iteration+1));
            if (region.isInside(xPositive)) {
                locations.add(xPositive);
            }
            if (region.isInside(xNegative)) {
                locations.add(xNegative);
            }
            if (region.isInside(yPositive)) {
                locations.add(yPositive);
            }
            if (region.isInside(yNegative)) {
                locations.add(yNegative);
            }
            if (region.isInside(zPositive)) {
                locations.add(zPositive);
            }
            if (region.isInside(zNegative)) {
                locations.add(zNegative);
            }
            completableFuture.complete(locations);
        });
        this.executorService.shutdown();
        return completableFuture;
    }

}
