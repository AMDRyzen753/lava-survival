package link.therealdomm.heldix.lavasurvival.util.countdown;

import link.therealdomm.heldix.lavasurvival.LavaSurvivalPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * an abstract implementation for easy countdowns
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Getter
public abstract class Countdown implements Runnable {


    private boolean running = false;
    private int initialTime;
    @Setter private int remainingTime;
    private BukkitTask task;

    /**
     * starts the countdown
     * @param remainingTime the time the countdown should take in seconds
     */
    public void startCountdown(int remainingTime) {
        this.running = true;
        this.initialTime = remainingTime;
        this.remainingTime = remainingTime;
        this.onStart();
        this.task = Bukkit.getScheduler().runTaskTimer(
                LavaSurvivalPlugin.getInstance(),
                () -> {
                    if (Countdown.this.remainingTime <= 0) {
                        Countdown.this.cancel();
                        Countdown.this.onEnd();
                        return;
                    }
                    Countdown.this.run();
                    Countdown.this.remainingTime--;
                },
                0,
                20
        );
    }

    /**
     * this method will be called once during the initialization of the countdown
     */
    public abstract void onStart();

    /**
     * this method will be called once when the countdown time is exceeded
     */
    public abstract void onEnd();

    /**
     * this method will cancel the countdown
     */
    public void cancel() {
        this.running = false;
        this.task.cancel();
    }

}
