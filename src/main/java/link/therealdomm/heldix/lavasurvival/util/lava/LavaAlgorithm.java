package link.therealdomm.heldix.lavasurvival.util.lava;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.scoreboard.ScoreType;
import link.therealdomm.heldix.lavasurvival.util.location.SimpleLocation;
import link.therealdomm.heldix.lavasurvival.util.region.CuboidRegion;
import link.therealdomm.heldix.lavasurvival.util.task.LocationIterator;
import lombok.*;
import org.bukkit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class LavaAlgorithm {

    @Getter private final List<SimpleLocation> lavaLocations = new ArrayList<>();
    private boolean borderDeployed = false;
    private final Location initialLocation;
    private final CuboidRegion region;
    @Getter private int iterationCount = 0;
    private int fails = 0;

    public LavaAlgorithm(Location initialLocation, CuboidRegion region) {
        this.initialLocation = initialLocation;
        this.region = region;
    }

    public void iterateOnce() {
        //this.dispatchWorldEdit();
        Bukkit.getScheduler().runTaskAsynchronously(
                LavaSurvivalPlugin.getInstance(),
                () -> {
                    try {
                        List<SimpleLocation> locations = LocationIterator.get()
                                .submitCalculation(
                                        CalculatorValues.builder()
                                                .region(this.region)
                                                .world(Objects.requireNonNull(this.initialLocation.getWorld()).getName())
                                                .algorithm(this)
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

    public synchronized void changeLocationsSynchronous(List<SimpleLocation> locations) {
        int maxRadius = LavaSurvivalPlugin.getInstance().getMainConfig().getMaxLavaRadius();
        if (maxRadius <= this.iterationCount && maxRadius != -1) {
            if (!this.borderDeployed) {
                this.borderDeployed = true;
                Bukkit.getScheduler().runTask(
                        LavaSurvivalPlugin.getInstance(),
                        () -> {
                            World world = this.initialLocation.getWorld();
                            WorldBorder worldBorder = world.getWorldBorder();
                            worldBorder.setCenter(this.initialLocation);
                            worldBorder.setSize(LavaSurvivalPlugin.getInstance().getMainConfig().getWorldBorderRadius());
                            worldBorder.setWarningDistance(15);
                            worldBorder.setDamageBuffer(0);
                            worldBorder.setSize(15, LavaSurvivalPlugin.getInstance().getMainConfig()
                                    .getWorldBorderShrinkTime().longValue());
                        }
                );
            }
            return;
        }
        this.iterationCount++;
        LavaSurvivalPlugin.getInstance().getScoreboardManager().updateScoreboards(ScoreType.LAVA_SIZE);
        for (SimpleLocation location : locations) {
            Bukkit.getScheduler().runTask(
                    LavaSurvivalPlugin.getInstance(),
                    () -> location.toLocation().getBlock().setType(Material.LAVA, false)
            );
        }
    }

    /*public void dispatchWorldEdit() {
        this.iterationCount++;
        LavaSurvivalPlugin.getInstance().getScoreboardManager().updateScoreboards(ScoreType.LAVA_SIZE);
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory()
                .getEditSession(new BukkitWorld(Objects.requireNonNull(this.initialLocation.getWorld())), -1);
        Pattern pattern = BukkitAdapter.adapt(Material.LAVA.createBlockData());
        BlockVector3 blockVector3 = BlockVector3Imp.at(
                this.initialLocation.getX(),
                this.initialLocation.getY(),
                this.initialLocation.getZ()
        );
        editSession.makeSphere(blockVector3, pattern, this.iterationCount, true);
        editSession.flushQueue();
    }*/

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalculatorValues {
        private CuboidRegion region;
        private String world;
        private LavaAlgorithm algorithm;
        private int xInitial;
        private int yInitial;
        private int zInitial;
        private int iteration;
    }

}
