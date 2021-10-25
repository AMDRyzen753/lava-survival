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
public class SetLocSubCommand implements SetupSubCommand {

    private final String name = "settloc";
    private final String permission = "command.setup.setloc";
    private final String description = "Set the map bounds";

    @Override
    public void onCommand(Player player, String[] strings) {
        Map currentMap = LavaSurvivalPlugin.getInstance().getMapManager().getCurrentMap();
        if (currentMap != null) {
            if (strings.length == 0) {
                player.sendMessage("/setup setloc <1|2>");
                return;
            }
            if (strings[0].equalsIgnoreCase("1")) {
                currentMap.getMapConfig().setFirstCorner(ConfigLocation.fromLocation(player.getLocation()));
                currentMap.saveConfig();
                player.sendMessage("§aThe first corner of the map was set!");
            } else if (strings[0].equalsIgnoreCase("2")) {
                currentMap.getMapConfig().setSecondCorner(ConfigLocation.fromLocation(player.getLocation()));
                currentMap.saveConfig();
                player.sendMessage("§aThe second corner of the map was set!");
            } else {
                player.sendMessage("/setup setloc <1|2>");
            }
            return;
        }
        player.sendMessage("§cThere is no map loaded!");
    }
}
