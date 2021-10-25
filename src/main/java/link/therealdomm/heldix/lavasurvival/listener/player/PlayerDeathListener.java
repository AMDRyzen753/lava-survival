package link.therealdomm.heldix.lavasurvival.listener.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.handler.MessageHandler;
import link.therealdomm.heldix.lavasurvival.handler.RespawnHandler;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.scoreboard.ScoreType;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author TheRealDomm
 * @since 23.10.2021
 */
public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();
        LavaPlayer lavaPlayer = LavaPlayer.getPlayer(player);
        lavaPlayer.setInGame(false);
        lavaPlayer.addDeath();
        lavaPlayer.uploadStats();
        if (LavaPlayer.getPlayers().stream().filter(LavaPlayer::isInGame).count() <= 1) {
            lavaPlayer.addWin();
            lavaPlayer.uploadStats();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.equals(player)) {
                    onlinePlayer.sendMessage(MessageHandler.getMessage("other.won", player.getDisplayName()));
                } else {
                    onlinePlayer.sendMessage(MessageHandler.getMessage("self.won"));
                }
            }
            GameState.getCurrentGameState().onNextGameState();
            return;
        }
        LavaSurvivalPlugin.getInstance().getScoreboardManager().updateScoreboards(ScoreType.PLAYERS_ALIVE);
        Bukkit.getScheduler().runTaskLater(LavaSurvivalPlugin.getInstance(), () -> RespawnHandler.respawn(player), 10L);
    }

}
