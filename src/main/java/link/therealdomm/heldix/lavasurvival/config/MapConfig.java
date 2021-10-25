package link.therealdomm.heldix.lavasurvival.config;

import link.therealdomm.heldix.lavasurvival.util.configuration.Config;
import link.therealdomm.heldix.lavasurvival.util.location.ConfigLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MapConfig implements Config {

    private double configVersion = 1.1D;
    private ConfigLocation spawnLocation = new ConfigLocation();
    private ConfigLocation specLocation = new ConfigLocation();
    private ConfigLocation firstCorner = new ConfigLocation();
    private ConfigLocation secondCorner = new ConfigLocation();
    private String mapName = "UNKNOWN";

}
