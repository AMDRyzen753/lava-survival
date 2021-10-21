package link.therealdomm.heldix.lavasurvival.util.command;

import lombok.Data;
import org.bukkit.command.CommandSender;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Data
public abstract class PluginCommand {

    private final String name;
    private String permission = "";
    private String[] aliases = new String[0];
    private String permissionMessage = "§cYou do not have permission to execute this command";

    public PluginCommand(String name) {
        this.name = name;
    }

    public PluginCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public PluginCommand(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }

    public PluginCommand(String name, String permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public PluginCommand(String name, String permission, String permissionMessage, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.aliases = aliases;
    }

    public abstract void onCommand(CommandSender sender, String s, String... strings);

}
