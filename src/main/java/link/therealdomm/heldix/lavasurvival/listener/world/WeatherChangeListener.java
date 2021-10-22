package link.therealdomm.heldix.lavasurvival.listener.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class WeatherChangeListener implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.getWorld().setThundering(false);
        event.getWorld().setStorm(false);
    }

}
