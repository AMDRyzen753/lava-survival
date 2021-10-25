package link.therealdomm.heldix.lavasurvival.command.setup;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.util.location.ConfigLocation;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author TheRealDomm
 * @since 25.10.2021
 */
@Getter
public class SetLobbySubCommand implements SetupSubCommand {

    private final String name = "setlobby";
    private final String permission = "command.setup.setlobby";
    private final String description = "Set the location for the waiting area";

    @Override
    public void onCommand(Player player, String[] strings) {
        LavaSurvivalPlugin.getInstance().getMainConfig()
                .setLobbySpawnLocation(ConfigLocation.fromLocation(player.getLocation()));
        LavaSurvivalPlugin.getInstance().saveMainConfig();
        player.sendMessage("§aThe lobby location was set!");
    }
}
