package org.jufyer.plugin.elementalWeather.config;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginConfig {

  private final JavaPlugin plugin;

  public PluginConfig(JavaPlugin plugin) {
    this.plugin = plugin;
    plugin.saveDefaultConfig();
  }

  public String getDefaultLanguage() {
    return plugin.getConfig().getString("default-language", "en_us");
  }
}

