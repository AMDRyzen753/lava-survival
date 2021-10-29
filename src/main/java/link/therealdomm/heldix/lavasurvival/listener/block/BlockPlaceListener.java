package link.therealdomm.heldix.lavasurvival.listener.block;

import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.state.impl.InGameState;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class BlockPlaceListener implements Listener {

    @Getter private static List<Block> buildBlocks = new ArrayList<>();

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (GameState.getCurrentGameState().getGameState().equals(EnumGameState.IN_GAME)) {
            if (Objects.requireNonNull(GameState.getGameState(InGameState.class)).isBuildTime()) {
                event.getPlayer().getInventory().getItemInMainHand().setAmount(2);
                buildBlocks.add(event.getBlockPlaced());
                return;
            }
        }
        event.setCancelled(true);
    }

}
