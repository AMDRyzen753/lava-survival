package link.therealdomm.heldix.lavasurvival.listener.entity;

import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author TheRealDomm
 * @since 16.10.2021
 */
public class EntityDamageListener implements Listener {

    @Setter private static boolean noDamage = false;

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (GameState.getCurrentGameState().getGameState().equals(EnumGameState.ENDING)) {
            event.setCancelled(true);
            return;
        }
        if (noDamage) {
            return;
        }
        if (event.getEntity() instanceof Player player) {
            if (LavaPlayer.getPlayer(player).isInGame()) {
                if (!event.getEntity().getLocation().getWorld().getWorldBorder().isInside(event.getEntity().getLocation())) {
                    return;
                }
                if (event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) ||
                        event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) ||
                        event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                    return;
                }
            }
        }
        event.setCancelled(true);
    }

}
