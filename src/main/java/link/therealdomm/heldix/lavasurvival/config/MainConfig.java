package link.therealdomm.heldix.lavasurvival.config;

import link.therealdomm.heldix.lavasurvival.scoreboard.ScoreType;
import link.therealdomm.heldix.lavasurvival.util.configuration.Config;
import link.therealdomm.heldix.lavasurvival.util.location.ConfigLocation;
import link.therealdomm.heldix.lavasurvival.util.mysql.MySQLData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MainConfig implements Config {

    private double configVersion = 1.1D;
    private MySQLData mySQLData = new MySQLData();
    private String lobbyWorldName = "lobby";
    private ConfigLocation lobbySpawnLocation = new ConfigLocation();
    private ConfigLocation restartLobbyLocation = new ConfigLocation();
    private Integer increaseLava = 10;
    private Integer lobbyTimer = 60;
    private Integer fullRoundTimer = 10;
    private Integer[] lobbyAnnounceTimes = new Integer[]{60, 30, 20, 10, 5, 3, 2, 1};
    private Integer restartTimer = 20;
    private Integer[] restartAnnounceTimes = new Integer[]{20, 10, 5, 3, 2, 1};
    private String lobbyGroupName = "Lobby";
    private Material buildingBlock = Material.NETHERRACK;
    private Integer buildTime = 60;
    private Integer[] buildAnnounceTimes = new Integer[]{60, 30, 20, 10, 5, 3, 2, 1};
    private Integer minPlayers = 4;
    private Integer maxPlayers = 24;
    private boolean usePremiumKick = true;
    private String scoreboardHeader = "§b§lHELDIX.NET";
    private Map<Integer, Map<ScoreType, String>> scoreboardConfig = new LinkedHashMap<>() {{
        put(11, createMap(ScoreType.TEXT, " "));
        put(10, createMap(ScoreType.TEXT, "&6Map: "));
        put(9, createMap(ScoreType.MAP, "{0}"));
        put(8, createMap(ScoreType.TEXT, " "));
        put(7, createMap(ScoreType.TEXT, "&6Spieler: "));
        put(6, createMap(ScoreType.PLAYER_COUNT, "{0}"));
        put(5, createMap(ScoreType.TEXT, " "));
        put(4, createMap(ScoreType.TEXT, "&6Überlebende: "));
        put(3, createMap(ScoreType.PLAYERS_ALIVE, "{0}"));
        put(2, createMap(ScoreType.TEXT, " "));
        put(1, createMap(ScoreType.TEXT, "&6Lava Radius"));
        put(0, createMap(ScoreType.LAVA_SIZE, "{0}"));
    }};

    private Map<ScoreType, String> createMap(ScoreType type, String text) {
        return new LinkedHashMap<>(){{put(type, text);}};
    }

}
