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
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
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
        Set<Class<? extends PluginCommand>> classes;
        try {
            Reflections reflections = new Reflections(packageName);
            classes = reflections.getSubTypesOf(PluginCommand.class);
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "Could not find any command due to an error!", e);
            return;
        }
        SimpleCommandMap commandMap;
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
        for (Class<?> clazz : classes) {
            if (PluginCommand.class.isAssignableFrom(clazz)) {
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
        Set<Class<? extends Listener>> classes;
        try {
            Reflections reflections = new Reflections(packageName);
            classes = reflections.getSubTypesOf(Listener.class);
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "Could not find any listener due to an error!", e);
            return;
        }
        for (Class<?> clazz : classes) {
            if (Listener.class.isAssignableFrom(clazz)) {
                try {
                    Listener listener = (Listener) clazz.getConstructors()[0].newInstance();
                    this.server.getPluginManager().registerEvents(listener, this.plugin);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    this.logger.log(Level.WARNING, "Could not register " + clazz.getSimpleName(), e);
                }
            }
        }
    }

}
