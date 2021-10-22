package link.therealdomm.heldix.lavasurvival.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class PlayerInteractEntityListener implements Listener {

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        event.setCancelled(true);
    }

}
