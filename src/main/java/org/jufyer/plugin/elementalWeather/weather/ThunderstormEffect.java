package org.jufyer.plugin.elementalWeather.weather;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jufyer.plugin.elementalWeather.language.LanguageManager;

public class ThunderstormEffect implements WeatherEffect {

  private final LanguageManager languageManager;
  private boolean active = false;

  public ThunderstormEffect(LanguageManager languageManager) {
    this.languageManager = languageManager;
  }

  @Override
  public void apply(World world) {
    active = true;
    for (Player player : world.getPlayers()) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 1)); //Speed II
      player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 10, 1)); //Speed II
      player.getWorld().strikeLightningEffect(player.getLocation()); //visual
      String msg = languageManager.get(player, "storm_energy");
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

