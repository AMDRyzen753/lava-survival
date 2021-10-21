package link.therealdomm.heldix.lavasurvival.util.register;

import link.therealdomm.heldix.lavasurvival.util.command.PluginCommand;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class PluginRegisterUtility {

    private final JavaPlugin plugin;
    private final Logger logger;
    private final Server server;
    private final String name;

    public PluginRegisterUtility(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.logger = javaPlugin.getLogger();
        this.server = javaPlugin.getServer();
        this.name = javaPlugin.getName();
    }

    public void registerCommands(String packageName) {
        Class[] classes;
        try {
            classes = this.getClassesOf(packageName);
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "Could not find any command due to an error!", e);
            return;
        }
        SimpleCommandMap commandMap = null;
        try {
            Field bukkitMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitMap.setAccessible(true);
            commandMap = (SimpleCommandMap) bukkitMap.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            this.logger.log(Level.SEVERE, "Could not access command map! No command will be registered!", e);
            return;
        }
        if (commandMap == null) {
            this.logger.log(Level.SEVERE, "Could not invoke command map! No command will be registered!");
            return;
        }
        for (Class clazz : classes) {
            if (clazz.isAssignableFrom(PluginCommand.class)) {
                try {
                    PluginCommand pluginCommand = (PluginCommand) clazz.getConstructors()[0].newInstance();
                    Command command = new Command(pluginCommand.getName()) {
                        @Override
                        public boolean execute(@NotNull CommandSender commandSender, @NotNull String s,
                                               @NotNull String[] strings) {
                            if (this.testPermission(commandSender)) {
                                pluginCommand.onCommand(commandSender, s, strings);
                            }
                            return true;
                        }
                    };
                    command.setAliases(Arrays.asList(pluginCommand.getAliases()));
                    command.setPermission(pluginCommand.getPermission());
                    command.setPermissionMessage(pluginCommand.getPermissionMessage());
                    commandMap.register(this.name, command);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    this.logger.log(Level.WARNING, "Could not register " + clazz.getSimpleName(), e);
                }
            }
        }
    }

    public void registerListeners(String packageName) {
        Class[] classes;
        try {
            classes = this.getClassesOf(packageName);
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "Could not find any listener due to an error!", e);
            return;
        }
        for (Class clazz : classes) {
            if (clazz.isAssignableFrom(Listener.class)) {
                try {
                    Listener listener = (Listener) clazz.getConstructors()[0].newInstance();
                    this.server.getPluginManager().registerEvents(listener, this.plugin);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    this.logger.log(Level.WARNING, "Could not register " + clazz.getSimpleName(), e);
                }
            }
        }
    }

    private Class[] getClassesOf(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> directories = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            directories.add(new File(resource.getFile()));
        }
        List<Class> classes = new ArrayList<>();
        for (File directory : directories) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(Class[]::new);
    }

    private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length()-6)));
            }
        }
        return classes;
    }

}
