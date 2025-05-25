package org.jufyer.plugin.elementalWeather.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jufyer.plugin.elementalWeather.Main;
import org.jufyer.plugin.elementalWeather.language.LanguageManager;
import org.jufyer.plugin.elementalWeather.scoreboard.WeatherEffectScoreboard;
import org.jufyer.plugin.elementalWeather.weather.RainEffect;
import org.jufyer.plugin.elementalWeather.weather.SunEffect;
import org.jufyer.plugin.elementalWeather.weather.ThunderstormEffect;
import org.jufyer.plugin.elementalWeather.weather.WeatherEffect;

public class PlayerEffectsListener implements Listener {

  private final LanguageManager languageManager;
  private final WeatherEffect sunEffect;
  private final SunEffect sunEffectCheck;
  private final WeatherEffect rainEffect;
  private final RainEffect rainEffectCheck;
  private final WeatherEffect thunderstormEffect;
  private final ThunderstormEffect thunderstormEffectCheck;

  WeatherEffectScoreboard scoreboard = new WeatherEffectScoreboard(Main.getLanguageManager());

  public PlayerEffectsListener(LanguageManager languageManager,
                               WeatherEffect sunEffect,
                               SunEffect sunEffectCheck,
                               WeatherEffect rainEffect,
                               RainEffect rainEffectCheck,
                               WeatherEffect thunderstormEffect,
                               ThunderstormEffect thunderstormEffectCheck) {
    this.languageManager = languageManager;
    this.sunEffect = sunEffect;
    this.sunEffectCheck = sunEffectCheck;
    this.rainEffect = rainEffect;
    this.rainEffectCheck = rainEffectCheck;
    this.thunderstormEffect = thunderstormEffect;
    this.thunderstormEffectCheck = thunderstormEffectCheck;
  }

  @EventHandler
  public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    World world = player.getWorld();

    if (world.hasStorm()) {
      if (world.isThundering()) {
        thunderstormEffect.apply(world);
        player.sendActionBar(languageManager.get(player, "storm_energy"));
      } else {
        rainEffect.apply(world);
        player.sendActionBar(languageManager.get(player, "rain_cooldown"));
      }
    } else {
      sunEffect.apply(world);
      player.sendActionBar(languageManager.get(player, "sun_warmth"));
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();

    if (sunEffectCheck.isActive()) {
      if (player.getLocation().getBlock().getLightFromSky() >= 13) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 0));
      }
    }else if (rainEffectCheck.isActive()) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 10, 0));
    } else if (thunderstormEffectCheck.isActive()) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 1)); //Speed II
      player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 10, 1));
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    World world = player.getWorld();

    if (world.hasStorm()) {
      if (world.isThundering()) {
        thunderstormEffect.apply(world);
        player.sendActionBar(languageManager.get(player, "storm_energy"));
        scoreboard.update(player, "storm", 2);
      } else {
        rainEffect.apply(world);
        player.sendActionBar(languageManager.get(player, "rain_cooldown"));
        scoreboard.update(player, "rain", 2);
      }
    } else {
      sunEffect.apply(world);
      player.sendActionBar(languageManager.get(player, "sun_warmth"));
      scoreboard.update(player, "sun", 2);
    }
  }
}
