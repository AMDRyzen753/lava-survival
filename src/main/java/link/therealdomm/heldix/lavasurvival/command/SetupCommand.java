package link.therealdomm.heldix.lavasurvival.command;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.command.setup.*;
import link.therealdomm.heldix.lavasurvival.util.command.PluginCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author TheRealDomm
 * @since 25.10.2021
 */
public class SetupCommand extends PluginCommand {

    private final Map<String, SetupSubCommand> commandMap = new LinkedHashMap<>();

    public SetupCommand() {
        super("setup", "command.setup");
        this.registerCommands(
                LoadMapSubCommand.class,
                SetLobbySubCommand.class,
                SetLocSubCommand.class,
                SetRestartSubCommand.class,
                SetSpawnSubCommand.class,
                SetSpecSpawnSubCommand.class,
                ToggleSubCommand.class
        );
    }

    @Override
    public void onCommand(CommandSender sender, String s, String... strings) {
        if (!(sender instanceof Player player)) {
            return;
        }
        if (strings.length == 0) {
            this.sendHelp(sender);
            return;
        }
        if (!strings[0].equalsIgnoreCase("toggle")) {
            if (!LavaSurvivalPlugin.isSetupEnabled()) {
                sender.sendMessage("§cPlease enable setup mode first. (/setup toggle)");
                return;
            }
        }
        SetupSubCommand setupSubCommand = this.getCommand(strings[0]);
        if (setupSubCommand == null) {
            this.sendHelp(sender);
            return;
        }
        if (!sender.hasPermission(setupSubCommand.getPermission())) {
            sender.sendMessage("§cYou do not have permission to execute this command.");
            return;
        }
        String[] cutOff = this.cutArgs(strings);
        setupSubCommand.onCommand(player, cutOff);
    }

    public SetupSubCommand getCommand(String command) {
        if (!this.commandMap.containsKey(command.toLowerCase(Locale.ROOT))) {
            return null;
        }
        try {
            return this.commandMap.get(command.toLowerCase(Locale.ROOT));
        } catch (Exception e) {
            return null;
        }
    }

    public String[] cutArgs(String[] initial) {
        if (initial == null || initial.length <= 1) {
            return new String[0];
        }
        List<String> commands = new LinkedList<>();
        for (int i = 1; i < initial.length; i++) {
            try {
                commands.add(initial[i]);
            } catch (Exception e) {
                LavaSurvivalPlugin.getInstance().getLogger().info("Could not find argument for position " + i);
            }
        }
        return commands.toArray(new String[0]);
    }

    @SafeVarargs
    public final void registerCommands(Class<? extends SetupSubCommand>... classes) {
        for (Class<? extends SetupSubCommand> clazz : classes) {
            try {
                SetupSubCommand subCommand = clazz.newInstance();
                this.commandMap.put(subCommand.getName().toLowerCase(Locale.ROOT), subCommand);
            } catch (Exception e) {
                LavaSurvivalPlugin.getInstance().getLogger().warning("Could not register setup command " +
                        clazz.getSimpleName());
            }
        }
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§cPossible commands:");
        for (SetupSubCommand value : this.commandMap.values()) {
            sender.sendMessage(" §a- §e/" + value.getName() + " §8- §7" + value.getDescription());
        }
    }

}
