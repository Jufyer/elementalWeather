package org.jufyer.plugin.elementalWeather.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jufyer.plugin.elementalWeather.language.LanguageManager;

public class WeatherEffectScoreboard {

  private final LanguageManager languageManager;

  public WeatherEffectScoreboard(LanguageManager languageManager) {
    this.languageManager = languageManager;
  }

  public void update(Player player, String effectKey, Integer descLength) {
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    if (manager == null) {
      Bukkit.getLogger().severe("ScoreboardManager ist nicht verfügbar!");
      return;
    }

    Scoreboard scoreboard = manager.getNewScoreboard();
    Objective objective = scoreboard.registerNewObjective(
      "weatherEffect", "dummy",
      languageManager.get(player, "scoreboard_title")
    );
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    String effectLabel = languageManager.get(player, "scoreboard_effect_label");
    String effectName = languageManager.get(player, "scoreboard_effect_" + effectKey);
    objective.getScore(effectLabel).setScore(descLength+2);
    objective.getScore(effectName).setScore(descLength+1);


    for (int i = descLength; i > 0; i--) {
      String effectDesc = languageManager.get(player, "scoreboard_desc_" + effectKey + i);
      objective.getScore(effectDesc).setScore(i);
    }

    player.setScoreboard(scoreboard);
  }
}
