package link.therealdomm.heldix.lavasurvival.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class BlockBurnListener implements Listener {

    @EventHandler
    public void onBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

}
