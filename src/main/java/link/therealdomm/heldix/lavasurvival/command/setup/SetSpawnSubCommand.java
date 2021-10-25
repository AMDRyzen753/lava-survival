package link.therealdomm.heldix.lavasurvival.command.setup;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.map.Map;
import link.therealdomm.heldix.lavasurvival.util.location.ConfigLocation;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author TheRealDomm
 * @since 25.10.2021
 */
@Getter
public class SetSpawnSubCommand implements SetupSubCommand {

    private final String name = "setspawn";
    private final String permission = "command.setup.setspawn";
    private final String description = "Set the map spawn";

    @Override
    public void onCommand(Player player, String[] strings) {
        Map currentMap = LavaSurvivalPlugin.getInstance().getMapManager().getCurrentMap();
        if (currentMap != null) {
            currentMap.getMapConfig().setSpawnLocation(ConfigLocation.fromLocation(player.getLocation()));
            currentMap.saveConfig();
            player.sendMessage("§aSet spawn for the map " + currentMap.getMapConfig().getMapName());
            return;
        }
        player.sendMessage("§cThere is no map loaded!");
    }
}
