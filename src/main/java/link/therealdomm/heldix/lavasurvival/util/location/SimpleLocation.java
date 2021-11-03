package link.therealdomm.heldix.lavasurvival.util.location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author TheRealDomm
 * @since 03.11.2021
 */
@Getter
@RequiredArgsConstructor
public class SimpleLocation implements Comparable<SimpleLocation>{

    private final String world;
    private final double x;
    private final double y;
    private final double z;


    @Override
    public int compareTo(@NotNull SimpleLocation o) {
        return this.x == o.getX() &&
                this.y == o.getY() &&
                this.z == o.getZ() &&
                this.world.equalsIgnoreCase(o.getWorld()) ?
                0 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleLocation that = (SimpleLocation) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0 && Objects.equals(world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }
}
