package link.therealdomm.heldix.lavasurvival.state.impl;

import link.therealdomm.heldix.lavasurvival.handler.MessageHandler;
import link.therealdomm.heldix.lavasurvival.map.Map;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.scoreboard.ScoreType;
import link.therealdomm.heldix.lavasurvival.state.EnumGameState;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.util.lava.LavaAlgorithm;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class InGameState extends GameState {

    @Getter private boolean buildTime = true;
    @Getter private LavaAlgorithm lavaAlgorithm;
    private BukkitTask lavaTask;
    private BukkitTask buildTask;

    public InGameState() {
        super(EnumGameState.IN_GAME);
        setCurrentGameState(this);
        for (LavaPlayer player : LavaPlayer.getPlayers()) {
            player.setInGame(true);
            player.addPlayedGame();
        }
        this.getPlugin().getScoreboardManager().updateScoreboards(ScoreType.PLAYERS_ALIVE);
    }

    public void initLavaTask() {
        Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().clear());
        Map currentMap = this.getPlugin().getMapManager().getCurrentMap();
        this.lavaAlgorithm = new LavaAlgorithm(currentMap.getCuboidRegion().getRandomLocation(),
                currentMap.getCuboidRegion());
        this.lavaTask = Bukkit.getScheduler().runTaskTimer(
                this.getPlugin(),
                this::runLavaTask,
                0L,
                20L*this.getPlugin().getMainConfig().getIncreaseLava()
        );
    }

    public void runLavaTask() {
        if (this.lavaAlgorithm != null) {
            this.lavaAlgorithm.iterateOnce();
        }
    }

    @Override
    public void onReset() {
        if (this.buildTask != null) {
            this.buildTask.cancel();
            this.buildTask = null;
        }
        if (this.lavaTask != null) {
            this.lavaTask.cancel();
            this.lavaTask = null;
        }
        this.buildTime = false;
    }

    @Override
    public void onNextGameState() {
        if (this.getGameState() == EnumGameState.IN_GAME) {
            this.onReset();
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.teleport(this.getPlugin().getMainConfig().getRestartLobbyLocation().toLocation()));
            new EndingGameState();
        }
    }

    @Override
    public void onInit() {
        Bukkit.getOnlinePlayers().forEach(player -> player.getInventory()
                .addItem(new ItemStack(this.getPlugin().getMainConfig().getBuildingBlock(), 1)));
        Bukkit.getOnlinePlayers().forEach(player -> player.teleport(
                this.getPlugin().getMapManager().getCurrentMap().getMapConfig().getSpawnLocation().toLocation()
        ));
        AtomicInteger integer = new AtomicInteger(this.getPlugin().getMainConfig().getBuildTime());
        this.buildTime = true;
        this.buildTask = Bukkit.getScheduler().runTaskTimer(
                this.getPlugin(),
                () -> {
                    if (integer.get() == 0) {
                        this.buildTask.cancel();
                        this.buildTask = null;
                        this.initLavaTask();
                        this.buildTime = false;
                        return;
                    }
                    if (Arrays.asList(this.getPlugin().getMainConfig().getBuildAnnounceTimes()).contains(integer.get())) {
                        Bukkit.broadcastMessage(MessageHandler.getMessage("ingame.buildtime.left", integer.get()));
                    }
                    integer.getAndDecrement();
                },
                0,
                20
        );
    }

}
