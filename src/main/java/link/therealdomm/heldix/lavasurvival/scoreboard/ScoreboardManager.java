package link.therealdomm.heldix.lavasurvival.scoreboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class ScoreboardManager {

    private Map<UUID, PlayerScoreboard> scoreboardMap = new LinkedHashMap<>();

    public PlayerScoreboard createScoreboard(UUID uuid, String header, Line... lines) {
        PlayerScoreboard playerScoreboard = new PlayerScoreboard(uuid, header, lines);
        this.scoreboardMap.put(uuid, playerScoreboard);
        return playerScoreboard;
    }

    public PlayerScoreboard getScoreboard(UUID uuid) {
        return this.scoreboardMap.getOrDefault(uuid, null);
    }

    public boolean hasScoreboard(UUID uuid) {
        return this.scoreboardMap.containsKey(uuid);
    }

    public void deleteScoreboard(UUID uuid) {
        this.scoreboardMap.remove(uuid);
    }

}
