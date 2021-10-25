package link.therealdomm.heldix.lavasurvival.command.setup;

import org.bukkit.entity.Player;

/**
 * @author TheRealDomm
 * @since 25.10.2021
 */
public interface SetupSubCommand {

    /**
     * describes the first arg in main command
     * @return the sub command name
     */
    String getName();

    /**
     * the permission to execute the sub command
     * @return the permission as {@link String}
     */
    String getPermission();

    /**
     * get the command description
     * @return the description as {@link String}
     */
    String getDescription();

    /**
     * will be called everytime a subcommand is executed
     * @param player the player that executes the command
     * @param strings the parameters of the sub command
     */
    void onCommand(Player player, String[] strings);

}
