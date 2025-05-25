package org.jufyer.plugin.elementalWeather.language;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LanguageManager {

  private final JavaPlugin plugin;
  private final Map<String, JSONObject> languages = new HashMap<>();
  private final String defaultLang;

  public LanguageManager(JavaPlugin plugin, String defaultLang) {
    this.plugin = plugin;
    this.defaultLang = defaultLang;
    loadLang("en_us");
  }

  private void loadLang(String code) {
    try (InputStreamReader reader = new InputStreamReader(
      plugin.getResource("lang/" + code + ".json"), StandardCharsets.UTF_8)) {

      JSONObject obj = (JSONObject) new JSONParser().parse(reader);
      languages.put(code, obj);

    } catch (Exception e) {
      plugin.getLogger().log(Level.WARNING, "Failed to load language file: " + code, e);
    }
  }

  public String get(String langCode, String key) {
    JSONObject lang = languages.getOrDefault(langCode, languages.get(defaultLang));
    Object value = lang.getOrDefault(key, key);
    return ChatColor.translateAlternateColorCodes('&', String.valueOf(value));
  }

  public String get(Player player, String key) {
    String code = player.getLocale(); // returns e.g., en_us
    return get(code, key);
  }
}
