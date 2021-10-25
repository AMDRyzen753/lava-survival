package link.therealdomm.heldix.lavasurvival.scoreboard;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.state.impl.InGameState;
import link.therealdomm.heldix.lavasurvival.util.lava.LavaAlgorithm;
import lombok.Getter;

import java.util.*;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class ScoreboardManager {

    private static final String[] SCORE_ENTRIES = new String[] {
            "§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"
    };

    private Map<UUID, PlayerScoreboard> scoreboardMap = new LinkedHashMap<>();
    @Getter private final List<Line> defaultLines = new ArrayList<>();
    @Getter private final String defaultHeader;

    public ScoreboardManager() {
        this.defaultHeader = LavaSurvivalPlugin.getInstance().getMainConfig().getScoreboardHeader();
        Set<Map.Entry<Integer, Map<ScoreType, String>>> entries = LavaSurvivalPlugin.getInstance()
                .getMainConfig().getScoreboardConfig().entrySet();
        int i = 0;
        for (Map.Entry<Integer, Map<ScoreType, String>> entry : entries) {
            if (i >= SCORE_ENTRIES.length) {
                break;
            }
            Map<ScoreType, String> value = entry.getValue();
            ScoreType scoreType = new ArrayList<>(value.keySet()).get(0);
            String text = new ArrayList<>(value.values()).get(0).replaceAll("&", "§");
            Line line = Line.builder().text(text)
                    .entry(SCORE_ENTRIES[i]).scoreType(scoreType).line(entry.getKey()).build();
            this.defaultLines.add(line);
            i++;
        }
    }

    public PlayerScoreboard createDefault(UUID uuid) {
        return this.createScoreboard(uuid, this.defaultHeader, this.defaultLines.toArray(Line[]::new));
    }

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

    public void updateScoreboards(ScoreType scoreType) {
        for (PlayerScoreboard value : this.scoreboardMap.values()) {
            String text;
            switch (scoreType) {
                case MAP:
                    text = this.format(
                            value.getLine(scoreType).getText(),
                            LavaSurvivalPlugin.getInstance().getMapManager().getCurrentMap().getMapConfig().getMapName()
                    );
                    break;
                case LAVA_SIZE:
                    if (GameState.getCurrentGameState().getGameState().equals(EnumGameState.IN_GAME)) {
                        LavaAlgorithm lavaAlgorithm = GameState.getGameState(InGameState.class).getLavaAlgorithm();
                        if (lavaAlgorithm != null) {
                            text = this.format(
                                    value.getLine(scoreType).getText(),
                                    lavaAlgorithm.getIterationCount()
                            );
                        } else {
                            text = this.format(
                                    value.getLine(scoreType).getText(),
                                    0
                            );
                        }
                    } else {
                        text = this.format(
                                value.getLine(scoreType).getText(),
                                0
                        );
                    }
                    break;
                case PLAYER_COUNT:
                    text = this.format(
                            value.getLine(scoreType).getText(),
                            LavaPlayer.getPlayers().size()
                    );
                    break;
                case PLAYERS_ALIVE:
                    text = this.format(
                            value.getLine(scoreType).getText(),
                            LavaPlayer.getPlayers().stream().filter(LavaPlayer::isInGame).count()
                    );
                    break;
                default:
                case TEXT:
                    text = value.getLine(scoreType).getText();
                    break;
            }
            text = text.replaceAll("&", "§");
            value.update(scoreType, text);
        }
    }

    public void updateScoreboards() {
        for (ScoreType value : ScoreType.values()) {
            this.updateScoreboards(value);
        }
    }

    private String format(String value, Object... replacements) {
        int i = 0;
        for (Object replacement : replacements) {
            value = value.replaceAll("\\{" + i + "}", replacement.toString());
            i++;
        }
        return value;
    }

}
