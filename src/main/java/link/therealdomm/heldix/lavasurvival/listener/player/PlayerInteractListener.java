package link.therealdomm.heldix.lavasurvival.listener.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(
                LavaSurvivalPlugin.getInstance().getMainConfig().getBuildingBlock()
        ) && !event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WOODEN_PICKAXE)) {
            event.setCancelled(true);
        }
    }

}
