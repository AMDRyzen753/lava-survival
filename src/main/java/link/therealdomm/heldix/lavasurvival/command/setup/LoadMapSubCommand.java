package link.therealdomm.heldix.lavasurvival.command.setup;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author TheRealDomm
 * @since 25.10.2021
 */
@Getter
public class LoadMapSubCommand implements SetupSubCommand {

    private final String name = "loadmap";
    private final String permission = "command.setup.loadmap";
    private final String description = "Load a map";

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length == 0) {
            player.sendMessage("§aPlease provide a map name.");
            return;
        }
        if (!LavaSurvivalPlugin.getInstance().getMapManager().loadMap(strings[0])) {
            player.sendMessage("§cCould not load map " + strings[0]);
            return;
        }
        player.teleport(LavaSurvivalPlugin.getInstance().getMapManager().getCurrentMap().getWorld().getSpawnLocation());
        player.sendMessage("§aYou have been teleported.");
    }
}
