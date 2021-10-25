package link.therealdomm.heldix.lavasurvival.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

/**
 * @author TheRealDomm
 * @since 24.10.2021
 */
public class RespawnHandler {

    public static void respawn(Player player) {
        try {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.RESPAWN);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            LavaSurvivalPlugin.getInstance().getLogger().log(Level.WARNING, "Could not respawn player!", e);
        }
    }

}
