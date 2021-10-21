package link.therealdomm.heldix.lavasurvival.player;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.util.model.StatsModel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Getter
public class LavaPlayer {

    private static final Map<UUID, LavaPlayer> PLAYER_MAP = new HashMap<>();

    public static void remove(UUID uuid) {
        PLAYER_MAP.remove(uuid);
    }

    public static LavaPlayer getPlayer(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        if (PLAYER_MAP.containsKey(uuid)) {
            return PLAYER_MAP.get(uuid);
        }
        return getPlayer(Bukkit.getPlayer(uuid));
    }

    public static LavaPlayer getPlayer(Player player) {
        if (player == null) {
            return null;
        }
        if (PLAYER_MAP.containsKey(player.getUniqueId())) {
            return PLAYER_MAP.get(player.getUniqueId());
        }
        LavaPlayer lavaPlayer = new LavaPlayer(player);
        PLAYER_MAP.put(player.getUniqueId(), lavaPlayer);
        return lavaPlayer;
    }

    private final LavaSurvivalPlugin plugin = LavaSurvivalPlugin.getInstance();
    private final Player player;
    private final UUID uuid;
    private final String name;
    private final String displayName;
    private StatsModel statsModel;

    public LavaPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.displayName = player.getDisplayName();
        this.plugin.getStatsRepo().getStats(this.uuid, statsModel -> {
            this.statsModel = statsModel;
            if (!this.statsModel.isExists()) {
                this.plugin.getStatsRepo().createPlayer(this.uuid);
            }
        });
    }



}
