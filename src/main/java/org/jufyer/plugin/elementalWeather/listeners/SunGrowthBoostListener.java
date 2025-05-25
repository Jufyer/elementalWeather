package org.jufyer.plugin.elementalWeather.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.jufyer.plugin.elementalWeather.weather.SunEffect;

import java.util.Random;

public class SunGrowthBoostListener implements Listener {

  private final SunEffect sunEffect;
  private boolean isProcessing = false;

  public SunGrowthBoostListener(SunEffect sunEffect) {
    this.sunEffect = sunEffect;
  }

  @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
      if (!sunEffect.isActive() || isProcessing) return;

      try {
        isProcessing = true;

        Block block = event.getBlock();
        block.applyBoneMeal(BlockFace.UP);
      } finally {
        isProcessing = false;
      }
  }
}
