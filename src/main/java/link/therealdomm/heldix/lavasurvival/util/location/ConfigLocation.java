package link.therealdomm.heldix.lavasurvival.util.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.Serializable;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigLocation implements Serializable {

    private String world = "world";
    private double x = 0;
    private double y = 100;
    private double z = 0;
    private float yaw = 0;
    private float pitch = 0;

    /**
     * create a {@link ConfigLocation} from a {@link Location}
     * @param l the location to create from
     * @return a new instance of the {@link ConfigLocation} with the parameters of l
     */
    public static ConfigLocation fromLocation(Location l) {
        return new ConfigLocation(
                l.getWorld().getName(),
                l.getX(),
                l.getY(),
                l.getZ(),
                l.getYaw(),
                l.getPitch()
        );
    }

    /**
     * parse the {@link ConfigLocation} to {@link Location}
     * @return the bukkit {@link Location}
     */
    public Location toLocation() {
        return new Location(
                Bukkit.getWorld(this.world),
                this.x,
                this.y,
                this.z,
                this.yaw,
                this.pitch
        );
    }

}
