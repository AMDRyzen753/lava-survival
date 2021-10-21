package link.therealdomm.heldix.lavasurvival.map;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class MapLoader {

    @Getter private static List<Map> availableMaps = new ArrayList<>();

    public static void loadMaps() {
        File file = new File(LavaSurvivalPlugin.getInstance().getDataFolder(), "maps");
        if (!file.exists()) {
            throw new IllegalStateException("Could not load maps folder! May corrupted?");
        }
        File[] mapFolders = file.listFiles((dir, name) -> new File(dir, name).isDirectory());
        if (mapFolders != null && mapFolders.length > 0) {
            for (File mapFolder : mapFolders) {
                availableMaps.add(new Map(mapFolder));
            }
        }
    }

    public static Map getRandomMap() {
        if (availableMaps.isEmpty()) {
            return null;
        }
        int i = ThreadLocalRandom.current().nextInt(0, availableMaps.size()-1);
        Map map;
        if ((map = availableMaps.get(i)) != null) {
            return map;
        }
        return null;
    }

    public static Map getMapByName(String name) {
        for (Map availableMap : availableMaps) {
            if (availableMap.getMapFolder().getName().equalsIgnoreCase(name)) {
                return availableMap;
            }
        }
        return null;
    }

}
