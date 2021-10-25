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
public class SetSpecSpawnSubCommand implements SetupSubCommand {

    private final String name = "setspecspawn";
    private final String permission = "command.setup.setspecspawn";
    private final String description = "Set the map spectator spawn";

    @Override
    public void onCommand(Player player, String[] strings) {
        Map currentMap = LavaSurvivalPlugin.getInstance().getMapManager().getCurrentMap();
        if (currentMap != null) {
            currentMap.getMapConfig().setSpecLocation(ConfigLocation.fromLocation(player.getLocation()));
            currentMap.saveConfig();
            player.sendMessage("§aSet spec spawn for the map " + currentMap.getMapConfig().getMapName());
            return;
        }
        player.sendMessage("§cThere is no map loaded!");
    }
}
