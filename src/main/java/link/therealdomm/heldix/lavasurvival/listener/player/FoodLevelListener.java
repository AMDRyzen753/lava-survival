package link.therealdomm.heldix.lavasurvival.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class FoodLevelListener implements Listener {

    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
        event.setCancelled(true);
    }

}
