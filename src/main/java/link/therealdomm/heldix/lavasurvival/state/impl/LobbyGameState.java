package link.therealdomm.heldix.lavasurvival.state.impl;

import link.therealdomm.heldix.lavasurvival.countdown.LobbyCountdown;
import link.therealdomm.heldix.lavasurvival.handler.MessageHandler;
import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class LobbyGameState extends GameState {

    private BukkitTask waitingTask;
    private LobbyCountdown lobbyCountdown;

    public LobbyGameState() {
        super(EnumGameState.LOBBY);
        setCurrentGameState(this);
    }

    public void startCountDown() {
        this.lobbyCountdown = new LobbyCountdown();
        this.lobbyCountdown.startCountdown(this.getPlugin().getMainConfig().getLobbyTimer());
    }

    @Override
    public void onReset() {
        if (this.waitingTask != null) {
            this.waitingTask.cancel();
            this.waitingTask = null;
        }
        if (this.lobbyCountdown != null && this.lobbyCountdown.isRunning()) {
            this.lobbyCountdown.cancel();
        }
    }

    @Override
    public void onNextGameState() {
        if (this.getGameState() == EnumGameState.LOBBY) {
            new InGameState();
        }
    }

    @Override
    public void onInit() {
        AtomicInteger integer = new AtomicInteger(0);
        this.waitingTask = Bukkit.getScheduler().runTaskTimer(
                this.getPlugin(),
                () -> {
                    if (integer.get() == 10) {
                        Bukkit.broadcastMessage(MessageHandler.getMessage("lobby.waiting"));
                        integer.set(0);
                    }
                    integer.getAndIncrement();
                },
                0,
                20L
        );
    }
}
