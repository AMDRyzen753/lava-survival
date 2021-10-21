package link.therealdomm.heldix.lavasurvival.config;

import link.therealdomm.heldix.lavasurvival.util.configuration.Config;
import link.therealdomm.heldix.lavasurvival.util.location.ConfigLocation;
import link.therealdomm.heldix.lavasurvival.util.mysql.MySQLData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MainConfig implements Config {

    private double configVersion = 1.0D;
    private MySQLData mySQLData = new MySQLData();
    private String lobbyWorldName = "lobby";
    private ConfigLocation lobbySpawnLocation = new ConfigLocation();
    private Integer increaseLava = 10;
    private Integer lobbyTimer = 60;
    private Integer[] lobbyAnnounceTimes = new Integer[]{60, 30, 20, 10, 5, 3, 2, 1};
    private Integer restartTimer = 20;
    private Integer[] restartAnnounceTimes = new Integer[]{20, 10, 5, 3, 2, 1};
    private String lobbyGroupName = "Lobby";
    private Material buildingBlock = Material.NETHERRACK;
    private Integer buildTime = 60;
    private Integer[] buildAnnounceTimes = new Integer[]{60, 30, 20, 10, 5, 3, 2, 1};

}
