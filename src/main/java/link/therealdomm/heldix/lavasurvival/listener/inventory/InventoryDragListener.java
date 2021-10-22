package link.therealdomm.heldix.lavasurvival.listener.inventory;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * @author TheRealDomm
 * @since 16.10.2021
 */
public class InventoryDragListener implements Listener {

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
    }

}
