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
    private Map<String, String> messages = new LinkedHashMap<>() {{
        put("prefix", "&7[&6LavaSurvival&7]");
        put("lobby.waiting", "%prefix% &cWarte auf mehr Spieler");
        put("lobby.countdown", "%prefix% &7Die Runde startet in &e{0} &7Sekunden.");
        put("ending.restart", "%prefix% &7Der Server startet in &e{0} &7Sekunden neu.");
        put("ingame.buildtime.left", "%prefix% &7Du hast noch &e{0} &7Sekunden zum bauen.");
        put("stats", "%prefix% Stats:\n  - Tode: {0} \n  - Gewonnene Runden: {1} \n  - Gespielte Runde: {2}");
        put("game.started", "%prefix% &aDu hast die Runde gestartet.");
        put("lobby.started_already", "%prefix% &cDie Runde wurde bereits gestartet.");
        put("kicked.by.premium", "%prefix% &aDu wurdest gekickt da ein Premiumspieler die volle Runde betreten hat.");
        put("round.full", "%prefix% &cDiese Runde ist voll!");
        put("round.full.get_premium", "%prefix% &cDiese Runde ist voll. Kaufe dir Premium um volle Runden betreten zu können.");
        put("self.won", "%prefix% &aDu hast diese Runde gewonnen.");
        put("other.won", "%prefix% &aDie Runde hat {0} gewonnen!");
    }};

}
