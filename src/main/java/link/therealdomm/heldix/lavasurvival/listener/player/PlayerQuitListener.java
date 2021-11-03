package link.therealdomm.heldix.lavasurvival.listener.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.handler.MessageHandler;
import link.therealdomm.heldix.lavasurvival.listener.entity.EntityDamageListener;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.state.impl.LobbyGameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        List<LavaPlayer> players = LavaPlayer.getPlayers().stream().filter(LavaPlayer::isInGame)
                .collect(Collectors.toList());
        if (players.size() == 1) {
            LavaPlayer winner = players.get(0);
            EntityDamageListener.setNoDamage(true);
            winner.addWin();
            winner.uploadStats();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.equals(winner.getPlayer())) {
                    onlinePlayer.sendMessage(MessageHandler.getMessage("other.won", winner.getDisplayName()));
                } else {
                    onlinePlayer.sendMessage(MessageHandler.getMessage("self.won"));
                }
            }
            GameState.getCurrentGameState().onNextGameState();
        }
        if (GameState.getGameState(LobbyGameState.class) == null) {
            if (Bukkit.getOnlinePlayers().size()-1 <= 0) {
                Bukkit.shutdown();
            }
            return;
        }
        if (LavaSurvivalPlugin.getInstance().getMainConfig().getMinPlayers() > Bukkit.getOnlinePlayers().size()) {
            if (Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).getLobbyCountdown() != null) {
                Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).onReset();
            }
        }
    }

}
