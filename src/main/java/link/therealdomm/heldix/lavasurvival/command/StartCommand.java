package link.therealdomm.heldix.lavasurvival.command;

import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.state.impl.LobbyGameState;
import link.therealdomm.heldix.lavasurvival.util.command.PluginCommand;
import org.bukkit.command.CommandSender;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class StartCommand extends PluginCommand {

    public StartCommand() {
        super("start", "lavasurvival.start");
    }

    @Override
    public void onCommand(CommandSender sender, String s, String... strings) {
        GameState.getGameState(LobbyGameState.class).startCountdown();

    }
}
