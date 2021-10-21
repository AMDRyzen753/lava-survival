package link.therealdomm.heldix.lavasurvival.handler;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import org.bukkit.entity.Player;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class MessageHandler {

    /**
     * get the messages with all color and prefix replacements and argument replacements defined in the message in the
     * pattern of {number} (first argument must always start with 0)
     *
     * Example: %prefix% &aHello {0}, welcome to this amazing server!
     *          The argument '{0}' would be e.g. replaced with {@link Player#getName()}
     * @param key the config key of the message to obtain
     * @param replacements the replacements if there are any
     * @return the fully formatted message
     */
    public static String getMessage(String key, Object... replacements) {
        String message = LavaSurvivalPlugin.getInstance().getMessageConfig()
                .getMessages().getOrDefault(key, "Message " + key + " not found!");
        String prefix = LavaSurvivalPlugin.getInstance().getMessageConfig().getMessages().getOrDefault("prefix", "");
        message = message.replaceAll("%prefix%", prefix).replaceAll("&", "§");
        int i = 0;
        for (Object replacement : replacements) {
            message = message.replaceAll("\\{" + i +"}", replacement.toString());
            i++;
        }
        return message;
    }

}
