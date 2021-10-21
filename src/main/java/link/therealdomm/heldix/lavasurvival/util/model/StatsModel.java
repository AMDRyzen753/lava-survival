package link.therealdomm.heldix.lavasurvival.util.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class StatsModel {

    private final UUID uuid;
    private boolean exists = false;
    private Integer wonGames = 0;
    private Integer deaths = 0;
    private Integer gamesPlayed = 0;

    public void increment(StatsEntry statsEntry) {
        switch (statsEntry) {
            case WON_GAMES -> this.wonGames++;
            case DEATHS -> this.deaths++;
            case GAMES_PLAYED -> this.gamesPlayed++;
        }
    }

    public void add(StatsEntry statsEntry, int amount) {
        switch (statsEntry) {
            case WON_GAMES -> this.wonGames += amount;
            case DEATHS -> this.deaths += amount;
            case GAMES_PLAYED -> this.gamesPlayed += amount;
        }
    }

    public enum StatsEntry {
        WON_GAMES,
        DEATHS,
        GAMES_PLAYED
    }

}
