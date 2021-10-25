package link.therealdomm.heldix.lavasurvival.command;

import link.therealdomm.heldix.lavasurvival.handler.MessageHandler;
import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.state.impl.LobbyGameState;
import link.therealdomm.heldix.lavasurvival.util.command.PluginCommand;
import org.bukkit.command.CommandSender;

import java.util.Objects;

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
        if (GameState.getCurrentGameState().getGameState() == EnumGameState.LOBBY) {
            if (Objects.requireNonNull(GameState.getGameState(LobbyGameState.class)).getLobbyCountdown() != null) {
                if (Objects.requireNonNull(GameState.getGameState(LobbyGameState.class))
                        .getLobbyCountdown().getRemainingTime() > 10) {
                    Objects.requireNonNull(GameState.getGameState(LobbyGameState.class))
                            .getLobbyCountdown().setRemainingTime(10);
                    sender.sendMessage(MessageHandler.getMessage("game.started"));
                } else {
                    sender.sendMessage(MessageHandler.getMessage("lobby.started_already"));
                }
            } else {
                sender.sendMessage(MessageHandler.getMessage("lobby.waiting"));
            }
        }
    }
}
