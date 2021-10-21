package link.therealdomm.heldix.lavasurvival.event;

import link.therealdomm.heldix.lavasurvival.util.region.CuboidRegion;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Data
public class CuboidEnterEvent extends Event implements Cancellable {

    private static HandlerList handlerList = new HandlerList();

    private final Player player;
    private final CuboidRegion cuboidRegion;
    private boolean cancelled = false;

    public CuboidEnterEvent(Player player, CuboidRegion cuboidRegion) {
        this.player = player;
        this.cuboidRegion = cuboidRegion;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
