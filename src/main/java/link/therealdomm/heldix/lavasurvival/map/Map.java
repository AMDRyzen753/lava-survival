package link.therealdomm.heldix.lavasurvival.map;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.config.MapConfig;
import link.therealdomm.heldix.lavasurvival.util.configuration.ConfigLoader;
import link.therealdomm.heldix.lavasurvival.util.region.CuboidRegion;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Getter
public class Map {

    private final File mapFolder;
    private World world;
    private CuboidRegion cuboidRegion;
    private MapConfig mapConfig;

    public Map(File file) {
        this.mapFolder = file;
        this.mapConfig = ConfigLoader.load(MapConfig.class, new File(file, "map_config.json"));
    }

    public boolean loadMap() {
        try {
            File destination = new File(Bukkit.getWorldContainer(), this.mapFolder.getName());
            if (!destination.exists() && !destination.mkdir()) {
                throw new IllegalStateException("Could not create map folder for loaded map!");
            }
            Path sourceDir = this.mapFolder.toPath();
            Files.walk(sourceDir).forEach(sourcePath -> {
                try {
                    Path targetPath = destination.toPath().resolve(sourceDir.relativize(sourcePath));
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    LavaSurvivalPlugin.getInstance().getLogger().log(Level.WARNING, "", e);
                }
            });
            World world = WorldCreator.name(this.mapFolder.getName()).createWorld();
            if (world != null) {
                this.world = world;
                this.cuboidRegion = new CuboidRegion(this.mapConfig.getFirstCorner().toLocation(),
                        this.mapConfig.getSecondCorner().toLocation());
                return true;
            }
            LavaSurvivalPlugin.getInstance().getLogger().warning("Could not load world " + this.mapFolder.getName());
            return false;
        } catch (IOException e) {
            LavaSurvivalPlugin.getInstance().getLogger().severe("Could not load world " + this.mapFolder.getName());
            return false;
        }
    }

    public void saveConfig() {
        ConfigLoader.save(this.mapConfig, new File(this.mapFolder, "map_config.json"));
    }
}
