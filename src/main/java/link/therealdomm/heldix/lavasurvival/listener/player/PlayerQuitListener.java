package link.therealdomm.heldix.lavasurvival.listener.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.state.impl.LobbyGameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LavaPlayer.remove(player.getUniqueId());
        LavaSurvivalPlugin.getInstance().getSpectatorHandler().removeSpectator(player);
        LavaSurvivalPlugin.getInstance().getScoreboardManager().deleteScoreboard(player.getUniqueId());
        LavaSurvivalPlugin.getInstance().getScoreboardManager().updateScoreboards();
        event.setQuitMessage(null);
        if (GameState.getGameState(LobbyGameState.class) == null) {
            return;
        }
        if (LavaSurvivalPlugin.getInstance().getMainConfig().getMinPlayers() > Bukkit.getOnlinePlayers().size()) {
            if (Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).getLobbyCountdown() != null) {
                Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).onReset();
                Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).onInit();
            }
        }
    }

}
