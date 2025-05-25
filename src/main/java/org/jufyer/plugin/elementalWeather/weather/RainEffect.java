package org.jufyer.plugin.elementalWeather.weather;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jufyer.plugin.elementalWeather.language.LanguageManager;

public class RainEffect implements WeatherEffect {

  private final LanguageManager languageManager;
  private boolean active = false;

  public RainEffect(LanguageManager languageManager) {
    this.languageManager = languageManager;
  }

  @Override
  public void apply(World world) {
    active = true;
    for (Player player : world.getPlayers()) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 10, 0));
      String msg = languageManager.get(player, "rain_cooldown");
      player.sendActionBar(msg);
    }
  }

  @Override
  public void stop(World world) {
    active = false;
    //later...
  }

  public boolean isActive() {
    return active;
  }
}
