package org.jufyer.plugin.elementalWeather;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.elementalWeather.config.PluginConfig;
import org.jufyer.plugin.elementalWeather.language.LanguageManager;
import org.jufyer.plugin.elementalWeather.listeners.PlayerEffectsListener;
import org.jufyer.plugin.elementalWeather.listeners.RainGrowthSlowListener;
import org.jufyer.plugin.elementalWeather.listeners.SunGrowthBoostListener;
import org.jufyer.plugin.elementalWeather.listeners.WeatherChangeListener;
import org.jufyer.plugin.elementalWeather.scoreboard.WeatherEffectScoreboard;
import org.jufyer.plugin.elementalWeather.weather.RainEffect;
import org.jufyer.plugin.elementalWeather.weather.SunEffect;
import org.jufyer.plugin.elementalWeather.weather.ThunderstormEffect;

public final class Main extends JavaPlugin {

  private static Main instance;
  private static LanguageManager languageManager;

  public static Main getInstance() {
    return instance;
  }

  public static LanguageManager getLanguageManager() {
    return languageManager;
  }

  @Override
  public void onEnable() {
    instance = this;
    new PluginConfig(this);

    // Init language support
    languageManager = new LanguageManager(this, "en_us");


    new WeatherEffectScoreboard(languageManager);

    // Init weather effects
    SunEffect sunEffect = new SunEffect(languageManager);
    RainEffect rainEffect = new RainEffect(languageManager);
    ThunderstormEffect thunderstormEffect = new ThunderstormEffect(languageManager);

    // Register listeners
    Bukkit.getPluginManager().registerEvents(new WeatherChangeListener(sunEffect, rainEffect, thunderstormEffect), this);

    Bukkit.getPluginManager().registerEvents(new PlayerEffectsListener(
      languageManager,
      sunEffect,
      sunEffect,
      rainEffect,
      rainEffect,
      thunderstormEffect,
      thunderstormEffect), this);

    Bukkit.getPluginManager().registerEvents(new SunGrowthBoostListener(sunEffect), this);

    Bukkit.getPluginManager().registerEvents(new RainGrowthSlowListener(rainEffect), this);

    getLogger().info("ElementalWeather has been enabled.");
  }

  @Override
  public void onDisable() {
    getLogger().info("ElementalWeather has been disabled.");
  }
}
