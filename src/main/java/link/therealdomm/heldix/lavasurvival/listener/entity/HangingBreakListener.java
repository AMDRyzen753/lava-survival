package link.therealdomm.heldix.lavasurvival.listener.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class HangingBreakListener implements Listener {

    @EventHandler
    public void onBreak(HangingBreakEvent event) {
        event.setCancelled(true);
    }

}
