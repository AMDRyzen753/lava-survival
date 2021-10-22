package link.therealdomm.heldix.lavasurvival.command;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.util.command.PluginCommand;
import link.therealdomm.heldix.lavasurvival.util.region.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TheRealDomm
 * @since 22.10.2021
 */
public class TestCommand extends PluginCommand {

    private static BukkitTask bukkitTask;

    public TestCommand() {
        super("test");
    }

    @Override
    public void onCommand(CommandSender sender, String s, String... strings) {
        if (bukkitTask != null) {
            bukkitTask.cancel();
            bukkitTask = null;
            return;
        }
        Player player = (Player) sender;
        Location initial = player.getLocation();
        AtomicInteger run = new AtomicInteger(0);
        bukkitTask = Bukkit.getScheduler().runTaskTimer(
                LavaSurvivalPlugin.getInstance(),
                () -> {
                    setBlock(getLocations(initial, run.get()));
                    run.getAndIncrement();
                },
                0,
                10
        );
    }

    public List<Location> getLocations(Location initial, int run) {
        List<Location> locations = new ArrayList<>();
        World world = initial.getWorld();
        int xC = initial.getBlockX();
        int yC = initial.getBlockY();
        int zC = initial.getBlockZ();
        for (int x = xC - run; x <= xC + run; x++) {
            for (int y = yC - run; y <= yC + run; y++) {
                for (int z = zC - run; z <= zC + run; z++) {
                    if ((xC - x) * (xC - x) + (yC - y) * (yC - y) + (zC - z) * (zC - z) <= (run*run)) {
                        Location location = new Location(world, x, y, z);
                        locations.add(location);
                    }
                }
            }
        }
        return locations;
    }

    public void setBlock(List<Location> locations) {
        for (Location location : locations) {
            location.getBlock().setType(Material.LAVA, false);
        }
    }

}
