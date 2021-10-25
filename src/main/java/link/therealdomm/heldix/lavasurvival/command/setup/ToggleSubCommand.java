package link.therealdomm.heldix.lavasurvival.command.setup;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * the sub command implementation that toggles the setup mode
 *
 * @author TheRealDomm
 * @since 16.10.2021
 */
@Getter
public class ToggleSubCommand implements SetupSubCommand {

    private final String name = "toggle";
    private final String permission = "command.setup.toggle";
    private final String description = "Enable and disable the setup mode";

    @Override
    public void onCommand(Player player, String[] strings) {
        if (LavaSurvivalPlugin.isSetupEnabled()) {
            player.sendMessage("§cSetup was disabled!");
            LavaSurvivalPlugin.getInstance().getMapManager().prepareRandomMap();
            LavaSurvivalPlugin.setSetupEnabled(false);
            GameState.initialize();
        }
        else {
            LavaSurvivalPlugin.setSetupEnabled(true);
            LavaSurvivalPlugin.getInstance().getMapManager().killMap();
            GameState.reset();
            player.sendMessage("§aSetup was enabled!");
        }
    }
}
