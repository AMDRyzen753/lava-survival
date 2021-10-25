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
public class SetRestartSubCommand implements SetupSubCommand {

    private final String name = "setrestart";
    private final String permission = "command.setup.setrestart";
    private final String description = "Set the restart lobby location";

    @Override
    public void onCommand(Player player, String[] strings) {
        LavaSurvivalPlugin.getInstance().getMainConfig()
                .setRestartLobbyLocation(ConfigLocation.fromLocation(player.getLocation()));
        LavaSurvivalPlugin.getInstance().saveMainConfig();
        player.sendMessage("§aRestart lobby location was set!");
    }

}
