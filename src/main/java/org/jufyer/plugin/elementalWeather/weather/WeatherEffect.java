package org.jufyer.plugin.elementalWeather.weather;

import org.bukkit.World;

public interface WeatherEffect {
  void apply(World world);
  void stop(World world);
}
