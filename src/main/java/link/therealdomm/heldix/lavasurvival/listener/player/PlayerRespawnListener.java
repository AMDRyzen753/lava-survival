package link.therealdomm.heldix.lavasurvival.listener.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
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
        if (!LavaPlayer.getPlayer(event.getPlayer().getUniqueId()).isInGame()) {
            LavaSurvivalPlugin.getInstance().getSpectatorHandler().setSpectator(event.getPlayer());
            event.setRespawnLocation(LavaSurvivalPlugin.getInstance().getMapManager()
                    .getCurrentMap().getMapConfig().getSpecLocation().toLocation());
        }
    }

}
