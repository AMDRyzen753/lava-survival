package link.therealdomm.heldix.lavasurvival.countdown;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.handler.MessageHandler;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.util.countdown.Countdown;
import org.bukkit.Bukkit;

import java.util.Arrays;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class LobbyCountdown extends Countdown {

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {
        GameState.getCurrentGameState().onNextGameState();
    }

    @Override
    public void run() {
        if (Arrays.asList(LavaSurvivalPlugin.getInstance().getMainConfig().getLobbyAnnounceTimes())
                .contains(this.getRemainingTime())) {
            Bukkit.broadcastMessage(MessageHandler.getMessage("lobby.countdown", this.getRemainingTime()));
        }
    }
}
