package link.therealdomm.heldix.lavasurvival.util.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Cleanup;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * the loader implementation to load simple json configs, requires GSON
 *
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class ConfigLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * save a config to a specific path
     * @param config instance to save
     * @param file to save to
     */
    public static void save(Config config, File file) {
        try {
            //@Cleanup BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            @Cleanup BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            GSON.toJson(config, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save config to " + file.getAbsolutePath(), e);
        }
    }

    /**
     * updates a configuration file based on the Config version identifier
     * @param clazz of config to be updated
     * @param file to be updated to
     * @param <T> the class of the config
     * @return the requested config as class instantiation
     */
    public static  <T extends Config> T update(Class<? extends T> clazz, File file) {
        if (file == null || !file.exists()) {
            throw new IllegalStateException("Could not update config, file not found!");
        }
        try {
            T config = clazz.newInstance();
            double version = config.getConfigVersion();
            config = GSON.fromJson(new BufferedReader(new FileReader(file)), clazz);
            if (version > config.getConfigVersion()) {
                config.setConfigVersion(version);
                save(config, file);
            }
            return config;
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException("Could not update config!");
        }
    }

    /**
     * loads a configuration from disk to a class pattern
     * @param clazz of config to be loaded
     * @param file to be loaded from
     * @param <T> the class of the config
     * @return the requested config as class instantiation
     */
    public static  <T extends Config> T load(Class<? extends T> clazz, File file) {
        if (file == null) {
            throw new IllegalStateException("Could not load config, file does not exist!");
        }
        try {
            T config = clazz.newInstance();
            double version = config.getConfigVersion();
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IllegalStateException("Could not create new config at " + file.getAbsolutePath());
                } else {
                    save(config, file);
                }
            }
            config = GSON.fromJson(new BufferedReader(new FileReader(file)), clazz);
            if (version > config.getConfigVersion()) {
                config = update(clazz, file);
            }
            return config;
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException("Could not load config " + file.getAbsolutePath(), e);
        }
    }

}
