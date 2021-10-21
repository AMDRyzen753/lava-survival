package link.therealdomm.heldix.lavasurvival.util.lava;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.util.region.CuboidRegion;
import link.therealdomm.heldix.lavasurvival.util.task.LocationIterator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class LavaAlgorithm {

    private final Location initialLocation;
    private final CuboidRegion region;
    private int iterationCount = 0;
    private int fails = 0;

    public LavaAlgorithm(Location initialLocation, CuboidRegion region) {
        this.initialLocation = initialLocation;
        this.region = region;
    }

    public void iterateOnce() {
        Bukkit.getScheduler().runTaskAsynchronously(
                LavaSurvivalPlugin.getInstance(),
                () -> {
                    try {
                        List<Location> locations = LocationIterator.get()
                                .submitCalculation(
                                        CalculatorValues.builder()
                                                .region(this.region)
                                                .world(Objects.requireNonNull(this.initialLocation.getWorld()).getName())
                                                .xInitial(this.initialLocation.getBlockX())
                                                .yInitial(this.initialLocation.getBlockY())
                                                .zInitial(this.initialLocation.getBlockZ())
                                                .iteration(this.iterationCount)
                                                .build())
                                .get();
                        if (locations != null) {
                            this.changeLocationsSynchronous(locations);
                            this.fails = 0;
                        } else {
                            this.fails++;
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        this.fails++;
                        LavaSurvivalPlugin.getInstance().getLogger().log(Level.WARNING, "", e);
                    }
                    if (this.fails > 5) {
                        Bukkit.broadcastMessage("§cSomething went wrong. Please inform an administrator!");
                        Bukkit.getScheduler().runTaskLater(
                                LavaSurvivalPlugin.getInstance(),
                                Bukkit::shutdown,
                                20*10
                        );
                        return;
                    }
                    if (this.fails > 0) {
                        this.iterateOnce();
                    }
                }
        );
    }

    public synchronized void changeLocationsSynchronous(List<Location> locations) {
        this.iterationCount++;
        for (Location location : locations) {
            Bukkit.getScheduler().runTask(
                    LavaSurvivalPlugin.getInstance(),
                    () -> {
                        location.getBlock().setType(Material.LAVA, false);
                        location.getBlock().getState().update(true, false);
                    }
            );
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalculatorValues {
        private CuboidRegion region;
        private String world;
        private int xInitial;
        private int yInitial;
        private int zInitial;
        private int iteration;
    }

}
