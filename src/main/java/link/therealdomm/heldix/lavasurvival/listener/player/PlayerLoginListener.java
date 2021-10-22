package link.therealdomm.heldix.lavasurvival.listener.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.handler.MessageHandler;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.state.impl.LobbyGameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!LavaSurvivalPlugin.getInstance().getMainConfig().isUsePremiumKick()) {
            return;
        }
        Player player = event.getPlayer();
        if (GameState.getGameState(LobbyGameState.class) == null) {
            return;
        }
        if (Bukkit.getOnlinePlayers().size() > LavaSurvivalPlugin.getInstance().getMainConfig().getMaxPlayers()) {
            if (player.hasPermission("lavasurvival.premiumkick")) {
                List<Player> possiblePlayers = Bukkit.getOnlinePlayers().stream()
                        .filter(p -> !p.hasPermission("lavasurvival.premiumkick")).collect(Collectors.toList());
                if (!possiblePlayers.isEmpty()) {
                    possiblePlayers.get(0).kickPlayer(MessageHandler.getMessage("kicked.by.premium"));
                    event.allow();
                } else {
                    event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageHandler.getMessage("round.full"));
                }
            } else {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageHandler.getMessage("round.full.get_premium"));
            }
        }
    }

}
