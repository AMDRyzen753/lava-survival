package link.therealdomm.heldix.lavasurvival.map;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import lombok.Getter;

import java.io.File;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class MapManager {

    private final LavaSurvivalPlugin plugin;
    private final File mapFolder;
    @Getter private Map currentMap;

    public MapManager(LavaSurvivalPlugin plugin, File mapFolder) {
        this.plugin = plugin;
        this.mapFolder = mapFolder;
        MapLoader.loadMaps();
    }

    public void prepareRandomMap() {
        Map map = MapLoader.getRandomMap();
        if (map != null) {
            this.currentMap = map;
        } else {
            throw new IllegalStateException("Could not find any map to prepare!");
        }
    }

    public boolean loadMap(String name) {
        Map map = MapLoader.getMapByName(name);
        if (map != null) {
            this.currentMap = map;
            return true;
        }
        return false;
    }

    public void killMap() {
        this.currentMap = null;
    }

}
