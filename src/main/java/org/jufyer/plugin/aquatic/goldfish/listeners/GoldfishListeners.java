package org.jufyer.plugin.aquatic.goldfish.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jufyer.plugin.aquatic.goldfish.entity.Goldfish;

import java.util.Random;

public class GoldfishListeners implements Listener {
  @EventHandler
  public void onEntitySpawn(EntitySpawnEvent event) {
    if (event.getEntity().getType().equals(EntityType.TROPICAL_FISH)){
      Random random = new Random();
      int i = random.nextInt(10);
      if (i == 4){
        Location loc = event.getEntity().getLocation();
        new Goldfish(loc);
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    if (event.getEntity().getType() == EntityType.SALMON && event.getEntity().getName().equals("Goldfish")  && event.getEntity().getPersistentDataContainer().getKeys().contains(Goldfish.GOLDFISH_KEY)) {
      for (ItemStack drop : event.getDrops()){
        drop.setAmount(0);
      }
      event.getEntity().getLocation().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.GOLD_NUGGET));
    }
  }
}
