package link.therealdomm.heldix.lavasurvival.state;

import com.google.gson.internal.Primitives;
import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import link.therealdomm.heldix.lavasurvival.state.impl.LobbyGameState;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TheRealDomm
 * @since 21.10.2021
 */
public abstract class GameState {

    @Getter @Setter private static GameState currentGameState = null;

    public static <T extends GameState> T getGameState(Class<? extends T> type) {
        if (currentGameState == null) {
            return null;
        }
        try {
            return Primitives.wrap(type).cast(currentGameState);
        } catch (Exception e) {
            return null;
        }
    }

    public static void initialize() {
        if (currentGameState == null) {
            new LobbyGameState();
        }
    }

    public static void reset() {
        if (currentGameState != null) {
            currentGameState.onReset();
            currentGameState = null;
        }
    }

    @Getter private final EnumGameState gameState;
    @Getter private final LavaSurvivalPlugin plugin;

    public GameState(EnumGameState gameState) {
        this.gameState = gameState;
        this.plugin = LavaSurvivalPlugin.getInstance();
        this.onInit();
    }

    public abstract void onReset();

    public abstract void onNextGameState();

    public abstract void onInit();

}
