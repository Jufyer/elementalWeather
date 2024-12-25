package org.jufyer.plugin.aquatic.whale.listeners;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jufyer.plugin.aquatic.whale.entity.Whale;

import java.util.Random;

public class spawnListener implements Listener {
  @EventHandler
  public void onEntitySpawn(EntitySpawnEvent event) {
    if (event.getEntity().getType().equals(EntityType.DOLPHIN)){
      Random random = new Random();
      int i = random.nextInt(5);
      if (i == 4){
        Location loc = event.getEntity().getLocation();
        new Whale(loc);
        event.setCancelled(true);
      }
    }
  }
}
