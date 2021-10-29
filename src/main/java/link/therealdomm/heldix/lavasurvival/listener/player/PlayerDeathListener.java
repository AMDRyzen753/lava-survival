package link.therealdomm.heldix.lavasurvival.listener.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.handler.MessageHandler;
import link.therealdomm.heldix.lavasurvival.listener.entity.EntityDamageListener;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import link.therealdomm.heldix.lavasurvival.scoreboard.ScoreType;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TheRealDomm
 * @since 23.10.2021
 */
public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();
        LavaPlayer lavaPlayer = LavaPlayer.getPlayer(player);
        lavaPlayer.setInGame(false);
        lavaPlayer.addDeath();
        lavaPlayer.uploadStats();
        long alive = LavaPlayer.getPlayers().stream().filter(LavaPlayer::isInGame).count();
        if (alive == 1) {
            List<LavaPlayer> players = LavaPlayer.getPlayers().stream().filter(LavaPlayer::isInGame)
                    .collect(Collectors.toList());
            if (players.isEmpty()) {
                Bukkit.broadcastMessage("§cA fatal error occurred, shutting down...!");
                Bukkit.getScheduler().runTaskLater(LavaSurvivalPlugin.getInstance(), Bukkit::shutdown, 20*5);
                return;
            }
            LavaPlayer winner = players.get(0);
            EntityDamageListener.setNoDamage(true);
            winner.addWin();
            winner.uploadStats();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.equals(winner.getPlayer())) {
                    onlinePlayer.sendMessage(MessageHandler.getMessage("other.won", winner.getDisplayName()));
                } else {
                    onlinePlayer.sendMessage(MessageHandler.getMessage("self.won"));
                }
            }
            GameState.getCurrentGameState().onNextGameState();
            return;
        }
        LavaSurvivalPlugin.getInstance().getScoreboardManager().updateScoreboards(ScoreType.PLAYERS_ALIVE);
        Bukkit.getScheduler().runTaskLater(
                LavaSurvivalPlugin.getInstance(),
                () -> {
                    try {
                        String path = "net.minecraft.server.v1_16_R3.";
                        Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
                        Object respawnEnum = Class.forName(path + "PacketPlayInClientCommand$EnumClientCommand")
                                .getEnumConstants()[0];
                        Constructor<?>[] constructors = Class
                                .forName(path + "PacketPlayInClientCommand").getConstructors();
                        for (Constructor<?> constructor : constructors) {
                            Class<?>[] args = constructor.getParameterTypes();
                            if (args.length == 1 && args[0].equals(respawnEnum.getClass())) {
                                Object packet = Class.forName(path + "PacketPlayInClientCommand")
                                        .getConstructor(args).newInstance(respawnEnum);
                                Object connection = nmsPlayer.getClass()
                                        .getField("playerConnection").get(nmsPlayer);
                                connection.getClass().getMethod("a", packet.getClass()).invoke(connection, packet);
                                break;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        LavaSurvivalPlugin.getInstance().getLogger().warning("Could not respawn player! :(");
                    }
                },
                5
        );
    }

}
