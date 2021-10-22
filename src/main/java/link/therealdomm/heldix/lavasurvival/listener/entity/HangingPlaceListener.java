package link.therealdomm.heldix.lavasurvival.listener.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class HangingPlaceListener implements Listener {

    @EventHandler
    public void onPlace(HangingPlaceEvent event) {
        event.setCancelled(true);
    }

}
