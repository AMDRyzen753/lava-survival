package link.therealdomm.heldix.lavasurvival.util.region;

import link.therealdomm.heldix.lavasurvival.event.CuboidEnterEvent;
import link.therealdomm.heldix.lavasurvival.event.CuboidLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

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
        if (!l1.getWorld().getName().equals(l2.getWorld().getName())) {
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

    public boolean isInside(Location location) {
        if (!this.worldName.equalsIgnoreCase(location.getWorld().getName())) {
            return false;
        }
        return this.isInside(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean isInside(Player player) {
        return this.isInside(player.getLocation());
    }

    public void registerListener(Plugin plugin) {
        if (!this.listening) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    public Location getRandomLocation() {
        int x = ThreadLocalRandom.current().nextInt(this.x1, this.x2);
        int y = ThreadLocalRandom.current().nextInt(this.y1, this.y2);
        int z = ThreadLocalRandom.current().nextInt(this.z1, this.z2);
        return new Location(Bukkit.getWorld(this.worldName), x, y, z);
    }

    public void unregisterListener() {
        if (this.listening) {
            HandlerList.unregisterAll(this);
            this.listening = false;
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!this.isInside(event.getFrom()) && this.isInside(Objects.requireNonNull(event.getTo()))) {
            CuboidEnterEvent cuboidEnterEvent = new CuboidEnterEvent(event.getPlayer(), this);
            Bukkit.getServer().getPluginManager().callEvent(cuboidEnterEvent);
            if (cuboidEnterEvent.isCancelled()) {
                Location from = event.getFrom();
                Location to = event.getTo();
                to.setX(from.getX());
                to.setY(from.getY());
                to.setZ(from.getZ());
                event.getPlayer().teleport(to);
            }
        }
        else if (this.isInside(event.getFrom()) && !this.isInside(Objects.requireNonNull(event.getTo()))) {
            CuboidLeaveEvent cuboidLeaveEvent = new CuboidLeaveEvent(event.getPlayer(), this);
            Bukkit.getServer().getPluginManager().callEvent(cuboidLeaveEvent);
            if (cuboidLeaveEvent.isCancelled()) {
                Location from = event.getFrom();
                Location to = event.getTo();
                to.setX(from.getX());
                to.setY(from.getY());
                to.setZ(from.getZ());
                event.getPlayer().teleport(to);
            }
        }
    }

}