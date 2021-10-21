package link.therealdomm.heldix.lavasurvival.repo;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.util.model.StatsModel;
import link.therealdomm.heldix.lavasurvival.util.mysql.MySQLConnector;
import link.therealdomm.heldix.lavasurvival.util.mysql.MySQLResult;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class StatsRepo {

    private final MySQLConnector connector;

    public StatsRepo() {
        this.connector = LavaSurvivalPlugin.getInstance().getConnector();
        this.createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS lava_survival_stats (" +
                "uuid VARCHAR(36), " +
                "won_games INT, " +
                "deaths INT," +
                "games_played INT, " +
                "PRIMARY KEY(uuid)) " +
                "ENGINE=InnoDB DEFAULT CHARSET=latin1";
        this.connector.asyncUpdate(sql);
    }

    public void createPlayer(UUID uuid) {
        String sql = "INSERT INTO lava_survival_stats (uuid, won_games, deaths, games_played) VALUES (?, ?, ?, ?)";
        this.connector.asyncUpdate(sql, uuid.toString(), 0, 0, 0);
    }

    public void getStats(UUID uuid, Consumer<StatsModel> whenLoaded) {
        String sql = "SELECT won_games, deaths, games_played FROM lava_survival_stats WHERE uuid=?";
        this.connector.asyncQuery(result -> {
            if (result != null && !result.getResults().isEmpty()) {
                MySQLResult.Result queryResult = result.getResults().get(0);
                StatsModel statsModel = new StatsModel(uuid);
                statsModel.setExists(true);
                statsModel.setWonGames(queryResult.getInteger("won_games"));
                statsModel.setDeaths(queryResult.getInteger("deaths"));
                statsModel.setGamesPlayed(queryResult.getInteger("games_played"));
                whenLoaded.accept(statsModel);
            } else {
                whenLoaded.accept(new StatsModel(uuid));
            }
        }, sql, uuid.toString());
    }

    public void updateStats(StatsModel statsModel) {
        String sql = "UPDATE lava_survival_stats SET won_games=?, deaths=?, games_played=? WHERE uuid=?";
        this.connector.asyncUpdate(sql, statsModel.getWonGames(), statsModel.getDeaths(), statsModel.getGamesPlayed(),
                 statsModel.getUuid().toString());
    }

}
