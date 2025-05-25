package org.jufyer.plugin.elementalWeather.weather;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jufyer.plugin.elementalWeather.language.LanguageManager;

public class SunEffect implements WeatherEffect, Listener {

  private final LanguageManager languageManager;
  private boolean active = false;

  public SunEffect(LanguageManager languageManager) {
    this.languageManager = languageManager;
  }

  @Override
  public void apply(World world) {
    active = true;
    for (Player player : world.getPlayers()) {
      if (player.getLocation().getBlock().getLightFromSky() >= 13) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 0));

        String message = languageManager.get(player, "sun_warmth");
        player.sendActionBar(message);
      }
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
