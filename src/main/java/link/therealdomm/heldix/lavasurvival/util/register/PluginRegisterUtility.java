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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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

    @SafeVarargs
    public final void registerCommands(Class<? extends PluginCommand>... classes) {
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
        for (Class<? extends PluginCommand> clazz : classes) {
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

    @SafeVarargs
    public final void registerListeners(Class<? extends Listener>... classes) {
        for (Class<? extends Listener> clazz : classes) {
            try {
                Listener listener = (Listener) clazz.getConstructors()[0].newInstance();
                this.server.getPluginManager().registerEvents(listener, this.plugin);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                this.logger.log(Level.WARNING, "Could not register " + clazz.getSimpleName(), e);
            }
        }
    }

}
