package link.therealdomm.heldix.lavasurvival.config;

import link.therealdomm.heldix.lavasurvival.util.configuration.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MessageConfig implements Config {

    private double configVersion = 1.0D;
    private Map<String, String> messages = new LinkedHashMap<String, String>() {{
        put("prefix", "&7[&6LavaSurvival&7]");
        put("lobby.waiting", "%prefix% &cWarte auf mehr Spieler");
        put("lobby.countdown", "%prefix% &7Die Runde startet in &e{0} &7Sekunden.");
        put("ending.restart", "%prefix% &7Der Server startet in &e{0} &7Sekunden neu.");
        put("ingame.buildtime.left", "%prefix% &7Du hast noch &e{0} &7Sekunden zum bauen.");
        put("", "%prefix% ");
        put("", "%prefix% ");
        put("", "%prefix% ");
        put("", "%prefix% ");
        put("", "%prefix% ");
        put("", "%prefix% ");
        put("", "%prefix% ");
        put("", "%prefix% ");
        put("", "%prefix% ");
    }};

}
