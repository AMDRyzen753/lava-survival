package link.therealdomm.heldix.lavasurvival.listener.block;

import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.state.impl.InGameState;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (GameState.getCurrentGameState().getGameState().equals(EnumGameState.IN_GAME)) {
            if (Objects.requireNonNull(GameState.getGameState(InGameState.class)).isBuildTime()) {
                event.getPlayer().getInventory().getItemInMainHand().setAmount(2);
                return;
            }
        }
        event.setCancelled(true);
    }

}
