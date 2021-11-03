package link.therealdomm.heldix.lavasurvival.listener.block;

import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (GameState.getCurrentGameState().getGameState().equals(EnumGameState.IN_GAME)) {
            if (BlockPlaceListener.getBuildBlocks().contains(event.getBlock())) {
                event.getBlock().setType(Material.AIR);
            }
        }
        event.setCancelled(true);
    }

}
