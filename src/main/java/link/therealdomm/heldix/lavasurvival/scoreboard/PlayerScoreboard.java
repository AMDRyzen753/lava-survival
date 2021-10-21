package link.therealdomm.heldix.lavasurvival.scoreboard;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public class PlayerScoreboard {

    private final UUID owner;
    private String header;
    private List<Line> lines;

    public PlayerScoreboard(UUID owner, String header, Line... lines) {
        this.owner = owner;
        this.header = header;
        this.lines = Arrays.asList(lines);
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void addLine(Line line) {
        if (this.lines.size() > 14) {
            LavaSurvivalPlugin.getInstance().getLogger().warning("Line limit reached. Skipping line " + line.getLine());
            return;
        }
        this.lines.add(line);
    }

    public Line getLine(int line) {
        List<Line> collect = this.lines.stream().filter(l -> l.getLine() == line).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return collect.get(0);
        }
        return null;
    }

    public Line getLine(ScoreType scoreType) {
        List<Line> collect = this.lines.stream().filter(l -> l.getScoreType().equals(scoreType))
                .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return collect.get(0);
        }
        return null;
    }

    public void removeLine(int line) {
        List<Line> collect = this.lines.stream().filter(l -> l.getLine() == line).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            this.lines.remove(collect.get(0));
        }
    }

    public void removeLine(Line line) {
        this.lines.remove(line);
    }

    public void update(ScoreType scoreType, String text) {
        Line line;
        if ((line = this.getLine(scoreType)) != null) {
            this.update(line.getLine(), text);
        }
    }

    public void update(int score, String text) {
        Player player = Bukkit.getPlayer(this.owner);
        if (player == null) {
            return;
        }
        Scoreboard scoreboard = player.getScoreboard();
        Line line;
        if ((line = this.getLine(score)) == null) {
            return;
        }
        Team team;
        if ((team = scoreboard.getTeam("t" + line.hashCode())) != null) {
            Objective objective = scoreboard.getObjective("aaa");
            Optional<String> entry = team.getEntries().stream().findFirst();
            if (text.length() <= 16) {
                team.setPrefix(text);
            } else if (text.length() <= 32) {
                entry.ifPresent(scoreboard::resetScores);
                String textOne = text.substring(0, 16);
                String textTwo = text.substring(16);
                team.setPrefix(textOne);
                team.addEntry(textTwo);
                objective.getScore(textTwo).setScore(line.getLine());
            } else {
                entry.ifPresent(scoreboard::resetScores);
                String textOne = text.substring(0, 16);
                String textTwo = text.substring(16, 32);
                String textThree = text.substring(32, text.length() >= 49 ? 48 : text.length());
                team.setPrefix(textOne);
                team.addEntry(textTwo);
                team.setSuffix(textThree);
                objective.getScore(textTwo).setScore(line.getLine());
            }
        }
    }

    public void setScoreboard() {
        Player player = Bukkit.getPlayer(this.owner);
        if (player == null) {
            return;
        }
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.registerNewObjective("aaa", "dummy", "NAME");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(this.header.length() > 32 ? this.header.substring(0, 32) : this.header);
        for (Line line : this.lines) {
            Team team = scoreboard.getTeam("t" + line.hashCode()) == null ?
                    scoreboard.registerNewTeam("t" + line.hashCode()) :
                    scoreboard.getTeam("t" + line.hashCode());
            String text = line.getText();
            if (text.length() <= 16) {
                team.setPrefix(text);
                team.addEntry(line.getEntry());
                objective.getScore(line.getEntry()).setScore(line.getLine());
            } else if (text.length() <= 32) {
                String textOne = text.substring(0, 16);
                String textTwo = text.substring(16);
                team.setPrefix(textOne);
                team.addEntry(textTwo);
                objective.getScore(textTwo).setScore(line.getLine());
            } else {
                String textOne = text.substring(0, 16);
                String textTwo = text.substring(16, 32);
                String textThree = text.substring(32, text.length() >= 49 ? 48 : text.length());
                team.setPrefix(textOne);
                team.addEntry(textTwo);
                team.setSuffix(textThree);
                objective.getScore(textTwo).setScore(line.getLine());
            }
        }
        player.setScoreboard(scoreboard);
    }

}
