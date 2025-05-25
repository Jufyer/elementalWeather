package org.jufyer.plugin.elementalWeather.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.jufyer.plugin.elementalWeather.weather.RainEffect;
import org.jufyer.plugin.elementalWeather.weather.SunEffect;

import java.util.Random;

public class RainGrowthSlowListener implements Listener {
  private final RainEffect rainEffect;

  public RainGrowthSlowListener(RainEffect rainEffect) {
    this.rainEffect = rainEffect;
  }

  @EventHandler
  public void onBlockGrow(BlockGrowEvent event) {
    if (!rainEffect.isActive()) return;

    Random random = new Random();
    boolean grow = random.nextBoolean();

    if (!grow) {
      event.setCancelled(true);
    }
  }
}
