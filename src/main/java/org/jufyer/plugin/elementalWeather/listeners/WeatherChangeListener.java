package org.jufyer.plugin.elementalWeather.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.jufyer.plugin.elementalWeather.Main;
import org.jufyer.plugin.elementalWeather.language.LanguageManager;
import org.jufyer.plugin.elementalWeather.scoreboard.WeatherEffectScoreboard;
import org.jufyer.plugin.elementalWeather.weather.WeatherEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WeatherChangeListener implements Listener {

  private final Map<UUID, WeatherEffect> activeEffects = new HashMap<>();

  private final WeatherEffect sunEffect;
  private final WeatherEffect rainEffect;
  private final WeatherEffect thunderstormEffect;

  WeatherEffectScoreboard scoreboard = new WeatherEffectScoreboard(Main.getLanguageManager());

  public WeatherChangeListener(WeatherEffect sunEffect, WeatherEffect rainEffect, WeatherEffect thunderstormEffect) {
    this.sunEffect = sunEffect;
    this.rainEffect = rainEffect;
    this.thunderstormEffect = thunderstormEffect;
  }

  @EventHandler
  public void onWeatherChange(WeatherChangeEvent event) {
    World world = event.getWorld();
    UUID worldId = world.getUID();

    if (activeEffects.containsKey(worldId)) {
      activeEffects.get(worldId).stop(world);
      activeEffects.remove(worldId);
    }

    // Wechsel zu Sonne oder Regen
    if (!event.toWeatherState()) {
      // Kein Regen mehr → Sonne
      sunEffect.apply(world);
      rainEffect.stop(world);
      thunderstormEffect.stop(world);
      activeEffects.put(worldId, sunEffect);

      for (Player player : Bukkit.getOnlinePlayers()) {
        scoreboard.update(player, "sun", 2);
      }

    } else {
      // Es beginnt zu regnen
      rainEffect.apply(world);
      sunEffect.stop(world);
      thunderstormEffect.stop(world);
      activeEffects.put(worldId, rainEffect);

      for (Player player : Bukkit.getOnlinePlayers()) {
        scoreboard.update(player, "rain", 2);
      }
    }
  }

  @EventHandler
  public void onThunderChange(ThunderChangeEvent event) {
    World world = event.getWorld();
    UUID worldId = world.getUID();

    // Beende vorherigen Effekt
    if (activeEffects.containsKey(worldId)) {
      activeEffects.get(worldId).stop(world);
      activeEffects.remove(worldId);
    }

    if (event.toThunderState()) {
      // Gewitter beginnt
      thunderstormEffect.apply(world);
      rainEffect.stop(world);
      sunEffect.stop(world);
      activeEffects.put(worldId, thunderstormEffect);

      for (Player player : Bukkit.getOnlinePlayers()) {
        scoreboard.update(player, "storm", 2);
      }
    }
  }
}

