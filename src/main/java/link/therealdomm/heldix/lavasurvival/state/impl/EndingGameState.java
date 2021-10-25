package link.therealdomm.heldix.lavasurvival.state.impl;

import link.therealdomm.heldix.lavasurvival.handler.CloudHandler;
import link.therealdomm.heldix.lavasurvival.handler.MessageHandler;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class EndingGameState extends GameState {

    private BukkitTask restartTask;

    public EndingGameState() {
        super(EnumGameState.ENDING);
        setCurrentGameState(this);
    }

    @Override
    public void onReset() {
        /* can be safely ignored, server will restart */
    }

    @Override
    public void onNextGameState() {
        /* can be safely ignored, last game state */
    }

    @Override
    public void onInit() {
        for (LavaPlayer player : LavaPlayer.getPlayers()) {
            player.getPlayer().sendMessage(
                    MessageHandler.getMessage(
                            "stats",
                            player.getStatsModel().getDeaths(),
                            player.getStatsModel().getWonGames(),
                            player.getStatsModel().getGamesPlayed()
                    )
            );
        }
        AtomicInteger integer = new AtomicInteger(this.getPlugin().getMainConfig().getRestartTimer());
        this.restartTask = Bukkit.getScheduler().runTaskTimer(
                this.getPlugin(),
                () -> {
                    if (integer.get() == 0) {
                        this.restartTask.cancel();
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            CloudHandler.sendPlayerToLobby(onlinePlayer);
                        }
                        Bukkit.getScheduler().runTaskLater(this.getPlugin(), Bukkit::shutdown, 20*10);
                        return;
                    }
                    if (Arrays.asList(this.getPlugin().getMainConfig().getRestartAnnounceTimes()).contains(integer.get())) {
                        Bukkit.broadcastMessage(MessageHandler.getMessage("ending.restart", integer.get()));
                    }
                    integer.getAndDecrement();
                },
                0L,
                20L
        );
    }
}
