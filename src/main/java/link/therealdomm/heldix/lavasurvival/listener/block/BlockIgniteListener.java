package link.therealdomm.heldix.lavasurvival.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class BlockIgniteListener implements Listener {

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

}
