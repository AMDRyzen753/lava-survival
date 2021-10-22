package link.therealdomm.heldix.lavasurvival;

import link.therealdomm.heldix.lavasurvival.config.MainConfig;
import link.therealdomm.heldix.lavasurvival.config.MessageConfig;
import link.therealdomm.heldix.lavasurvival.map.MapManager;
import link.therealdomm.heldix.lavasurvival.repo.StatsRepo;
import link.therealdomm.heldix.lavasurvival.scoreboard.ScoreboardManager;
import link.therealdomm.heldix.lavasurvival.state.GameState;
import link.therealdomm.heldix.lavasurvival.util.configuration.ConfigLoader;
import link.therealdomm.heldix.lavasurvival.util.mysql.MySQLConnector;
import link.therealdomm.heldix.lavasurvival.util.register.PluginRegisterUtility;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.io.File;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Getter
@Author("TheRealDomm")
@ApiVersion(ApiVersion.Target.v1_16)
@Plugin(name = "LavaSurvival", version = "1.0.0")
public class LavaSurvivalPlugin extends JavaPlugin {

    @Getter private static LavaSurvivalPlugin instance;
    private PluginRegisterUtility registerUtility;
    private MapManager mapManager;
    private MainConfig mainConfig;
    private MessageConfig messageConfig;
    private MySQLConnector connector;
    private StatsRepo statsRepo;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        instance = this;
        if (!this.getDataFolder().exists() && !this.getDataFolder().mkdir()) {
            throw new IllegalStateException("Could not create data folder!");
        }
        File mapFolder = new File(this.getDataFolder(), "maps");
        if (!mapFolder.exists() && !mapFolder.mkdir()) {
            throw new IllegalStateException("Could not create map folder!");
        }
        this.mapManager = new MapManager(this, mapFolder);
        this.mapManager.prepareRandomMap();
        if (!this.mapManager.getCurrentMap().loadMap()) {
            throw new IllegalStateException("Could not load map " + this.mapManager.getCurrentMap().getMapFolder().getName());
        }
        this.mainConfig = ConfigLoader.load(MainConfig.class, new File(this.getDataFolder(), "config.json"));
        this.messageConfig = ConfigLoader.load(MessageConfig.class, new File(this.getDataFolder(), "messages.json"));
        this.connector = new MySQLConnector(this.mainConfig.getMySQLData());
        this.statsRepo = new StatsRepo();
        this.scoreboardManager = new ScoreboardManager();
        this.registerUtility = new PluginRegisterUtility(this);
        this.registerUtility.registerCommands("link.therealdomm.heldix.lavasurvival.command");
        this.registerUtility.registerListeners("link.therealdomm.heldix.lavasurvival.listener");
        GameState.initialize();
    }

    @Override
    public void onDisable() {

    }


}
