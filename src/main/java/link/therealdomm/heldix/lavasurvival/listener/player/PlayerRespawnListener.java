package link.therealdomm.heldix.lavasurvival.listener.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author TheRealDomm
 * @since 23.10.2021
 */
public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (GameState.getCurrentGameState().getGameState() != EnumGameState.IN_GAME) {
            event.setRespawnLocation(LavaSurvivalPlugin.getInstance().getMainConfig()
                    .getRestartLobbyLocation().toLocation());
            return;
        }
        if (!LavaPlayer.getPlayer(event.getPlayer().getUniqueId()).isInGame()) {
            LavaSurvivalPlugin.getInstance().getSpectatorHandler().setSpectator(event.getPlayer());
            event.setRespawnLocation(LavaSurvivalPlugin.getInstance().getMapManager()
                    .getCurrentMap().getMapConfig().getSpecLocation().toLocation());
        }
    }

}
