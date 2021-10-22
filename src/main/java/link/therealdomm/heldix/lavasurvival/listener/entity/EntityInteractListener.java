package link.therealdomm.heldix.lavasurvival.listener.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class EntityInteractListener implements Listener {

    @EventHandler
    public void onInteract(EntityInteractEvent event) {
        event.setCancelled(true);
    }

}
