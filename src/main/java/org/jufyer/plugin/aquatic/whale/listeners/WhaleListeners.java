package org.jufyer.plugin.aquatic.whale.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.jufyer.plugin.aquatic.shark.entity.Shark;
import org.jufyer.plugin.aquatic.whale.entity.Whale;

public class WhaleListeners implements Listener {
  @EventHandler
  public void onEntityPickupItem(EntityPickupItemEvent event) {
    if (event.getEntity().getPersistentDataContainer().has(Whale.WHALE_KEY)) {
      event.setCancelled(true);
    }
  }
}
