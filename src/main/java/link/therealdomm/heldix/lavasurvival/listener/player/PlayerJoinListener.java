package link.therealdomm.heldix.lavasurvival.listener.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.state.impl.LobbyGameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);
        event.setJoinMessage(null);
        if (GameState.getGameState(LobbyGameState.class) == null) {
            LavaSurvivalPlugin.getInstance().getSpectatorHandler().setSpectator(player);
            player.teleport(LavaSurvivalPlugin.getInstance().getMapManager().getCurrentMap()
                    .getMapConfig().getSpecLocation().toLocation());
            return;
        }
        LavaPlayer.getPlayer(player);
        player.teleport(LavaSurvivalPlugin.getInstance().getMainConfig().getLobbySpawnLocation().toLocation());
        int random = ThreadLocalRandom.current().nextInt(2, 7);
        Bukkit.getScheduler().runTaskLater(
                LavaSurvivalPlugin.getInstance(),
                () -> {
                    LavaSurvivalPlugin.getInstance().getScoreboardManager().createDefault(player.getUniqueId())
                            .setScoreboard();
                    LavaSurvivalPlugin.getInstance().getScoreboardManager().updateScoreboards();
                },
                random
        );
        if (LavaSurvivalPlugin.getInstance().getMainConfig().getMinPlayers() <= Bukkit.getOnlinePlayers().size()) {
            if (Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).getLobbyCountdown() == null) {
                Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).getWaitingTask().cancel();
                Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).startCountdown();
            }
        }
        if (LavaSurvivalPlugin.getInstance().getMainConfig().getMaxPlayers() <= Bukkit.getOnlinePlayers().size()) {
            if (Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).getLobbyCountdown()
                    .getRemainingTime() > LavaSurvivalPlugin.getInstance().getMainConfig().getFullRoundTimer()) {
                Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).getLobbyCountdown()
                        .setRemainingTime(LavaSurvivalPlugin.getInstance().getMainConfig().getFullRoundTimer());
            }
        }
    }

}
