package link.therealdomm.heldix.lavasurvival.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class BlockFromToListener implements Listener {

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        event.setCancelled(true);
    }

}
