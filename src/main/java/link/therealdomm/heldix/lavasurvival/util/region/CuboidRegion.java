package link.therealdomm.heldix.lavasurvival.util.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class CuboidRegion implements Serializable, Listener {

    protected String worldName;
    protected int x1, y1, z1;
    protected int x2, y2, z2;
    private boolean listening = false;

    public CuboidRegion(Location l1, Location l2) {
        if (!Objects.requireNonNull(l1.getWorld()).getName().equals(Objects.requireNonNull(l2.getWorld()).getName())) {
            throw new IllegalArgumentException("Locations must be in the same world!");
        }
        this.worldName = l1.getWorld().getName();
        this.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        this.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        this.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        this.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        this.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        this.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
    }

    public CuboidRegion(String worldName, int x1, int x2, int y1, int y2, int z1, int z2) {
        this.worldName = worldName;
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    public CuboidRegion redefine(String worldName, int x1, int x2, int y1, int y2, int z1, int z2) {
        this.worldName = worldName;
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
        return this;
    }

    public CuboidRegion redefine(Location l1, Location l2) {
        if (!Objects.requireNonNull(l1.getWorld()).getName().equals(Objects.requireNonNull(l2.getWorld()).getName())) {
            throw new IllegalArgumentException("Locations must be in the same world!");
        }
        this.worldName = l1.getWorld().getName();
        this.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        this.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        this.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        this.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        this.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        this.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        return this;
    }

    public boolean isInside(int x, int y, int z) {
        return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2;
    }

    public boolean isInside(int offSet, int x, int y, int z) {
        return x >= this.x1+offSet && x <= this.x2-offSet &&
                y >= this.y1+offSet && y <= this.y2-offSet &&
                z >= this.z1+offSet && z <= this.z2-offSet;
    }

    public boolean isInside(Location location) {
        if (!this.worldName.equalsIgnoreCase(Objects.requireNonNull(location.getWorld()).getName())) {
            return false;
        }
        return this.isInside(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean isInside(Player player) {
        return this.isInside(player.getLocation());
    }

    public Location getRandomLocation() {
        int x = ThreadLocalRandom.current().nextInt(this.x1, this.x2);
        int y = ThreadLocalRandom.current().nextInt(this.y1, this.y2);
        int z = ThreadLocalRandom.current().nextInt(this.z1, this.z2);
        return new Location(Bukkit.getWorld(this.worldName), x, y, z);
    }

}
