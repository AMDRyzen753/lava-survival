package link.therealdomm.heldix.lavasurvival.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    private String text;
    private String entry;
    private ScoreType scoreType;
    private int line;

}
