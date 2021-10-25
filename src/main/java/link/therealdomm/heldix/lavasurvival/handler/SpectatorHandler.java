package link.therealdomm.heldix.lavasurvival.handler;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.player.LavaPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * handler class for the spectators during game
 *
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class SpectatorHandler {

    @Getter private List<Player> spectators = new ArrayList<>();

    /**
     * removes a spectator from the spec list
     * @param player to be removed
     */
    public void removeSpectator(Player player) {
        this.spectators.remove(player);
    }

    /**
     * adds a player to spectator list and makes him invisible for the players still in game
     * @param player to be added as spectator
     */
    public void setSpectator(Player player) {
        if (player == null) {
            return;
        }
        player.getInventory().clear();
        player.setAllowFlight(true);
        this.spectators.add(player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (this.spectators.contains(onlinePlayer)) {
                player.showPlayer(LavaSurvivalPlugin.getInstance(), onlinePlayer);
            } else {
                if (LavaPlayer.hasPlayer(onlinePlayer.getUniqueId())) {
                    if (LavaPlayer.getPlayer(onlinePlayer).isInGame()) {
                        onlinePlayer.hidePlayer(LavaSurvivalPlugin.getInstance(), player);
                    }
                }
            }
        }
    }



}
