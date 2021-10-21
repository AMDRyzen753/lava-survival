package link.therealdomm.heldix.lavasurvival.handler;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class CloudHandler {

    /**
     * boots up the next GameServer of the currently running type if using the SimpleCloud Cloud-System
     */
    public static void bootNextServer() {
        try {
            Class<?> iCloudServiceGroupClass = Class.forName("eu.thesimplecloud.api.servicegroup.ICloudServiceGroup");
            Class<?> iCloudServiceClass = Class.forName("eu.thesimplecloud.api.service.ICloudService");
            Class<?> cloudPluginClass = Class.forName("eu.thesimplecloud.plugin.startup.CloudPlugin");
            Method getCloudPluginInstance = cloudPluginClass.getDeclaredMethod("getInstance");
            Object cloudPluginInstance = getCloudPluginInstance.invoke(cloudPluginClass);
            Method getThisService = cloudPluginClass.getMethod("thisService");
            Object iCloudServiceInstance = getThisService.invoke(cloudPluginInstance);
            Method getServiceGroup = iCloudServiceClass.getMethod("getServiceGroup");
            Object iCloudServiceGroupInstance = getServiceGroup.invoke(iCloudServiceInstance);
            Method startNewService = iCloudServiceGroupClass.getMethod("startNewService");
            startNewService.invoke(iCloudServiceGroupInstance);
            LavaSurvivalPlugin.getInstance().getLogger().info("Booted new Server!");
        } catch (Exception e) {
            LavaSurvivalPlugin.getInstance().getLogger().warning("CloudAPI not found! Ignoring it!");
        }
    }

    /**
     * send the player to the Lobby group specified in the config.json if using SimpleCloud Cloud-System, otherwise the
     * player will be kicked from the server
     * @param player to be sent to lobby
     */
    public static void sendPlayerToLobby(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        try {
            Class<?> iCloudPlayerClass = Class.forName("eu.thesimplecloud.api.player.ICloudPlayer");
            Class<?> cloudApiClass = Class.forName("eu.thesimplecloud.api.CloudAPI");
            Class<?> iCloudPlayerManagerClass = Class.forName("eu.thesimplecloud.api.player.ICloudPlayerManager");
            Method getCloudInstance = cloudApiClass.getDeclaredMethod("getInstance");
            Object cloudApiInstance = getCloudInstance.invoke(cloudApiClass);
            Method getPlayerManager = cloudApiClass.getMethod("getCloudPlayerManager");
            Object iCloudPlayerManagerInstance = getPlayerManager.invoke(cloudApiInstance);
            Method getCachedCloudPlayer = iCloudPlayerManagerClass.getMethod("getCachedCloudPlayer", UUID.class);
            Object iCloudPlayer = getCachedCloudPlayer.invoke(iCloudPlayerManagerInstance, player.getUniqueId());
            if (iCloudPlayer != null) {
                String lobby = LavaSurvivalPlugin.getInstance().getMainConfig().getLobbyGroupName();
                Class<?> iCloudServiceManager = Class.forName("eu.thesimplecloud.api.service.ICloudServiceManager");
                Class<?> iCloudService = Class.forName("eu.thesimplecloud.api.service.ICloudService");
                Method getCloudServiceManager = cloudApiClass.getMethod("getCloudServiceManager");
                Object cloudServiceManagerInstance = getCloudServiceManager.invoke(cloudApiInstance);
                Method getCloudLobbyService = iCloudServiceManager.getMethod("getCloudServicesByGroupName", String.class);
                Object cloudServiceInstance = getCloudLobbyService.invoke(cloudServiceManagerInstance, lobby);
                Method connect = iCloudPlayerClass.getMethod("connect", iCloudService);
                connect.invoke(iCloudPlayer, cloudServiceInstance);
                LavaSurvivalPlugin.getInstance().getLogger().info("Moved " + player.getName() + " to lobby server!");
            } else {
                LavaSurvivalPlugin.getInstance().getLogger().warning("Player was not found! Could not move " +
                        "" + player.getName() + " to lobby server. Kicking him instead!");
                player.kickPlayer("");
            }
        } catch (Exception e) {
            player.kickPlayer("");
        }
    }

}
