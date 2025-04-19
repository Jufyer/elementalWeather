package org.jufyer.plugin.aquatic.goldfish.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jufyer.plugin.aquatic.Main;
import org.jufyer.plugin.aquatic.goldfish.entity.Goldfish;
import org.jufyer.plugin.aquatic.nibblers.entity.Nibbler;

import java.util.HashMap;
import java.util.Map;
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

  @EventHandler
  public void onPlayerBucketEntity(PlayerBucketEntityEvent event) {
    Entity entity = event.getEntity();
    ItemStack newBucket = event.getEntityBucket();
    if (entity.getType() == EntityType.SALMON && entity.getName().equals("Goldfish") && entity.getPersistentDataContainer().getKeys().contains(Goldfish.GOLDFISH_KEY)){
      ItemStack Nibbler_Bucket = new ItemStack(Material.SALMON_BUCKET);
      ItemMeta meta = Nibbler_Bucket.getItemMeta();
      meta.setCustomModelData(Main.CMDGoldfishBucket);
      meta.setDisplayName("§rBucket of Goldfish");
      Nibbler_Bucket.setItemMeta(meta);
      newBucket.setItemMeta(meta);
    }
  }

  private final Map<Player, Long> lastPlacedBlockTimes = new HashMap<>();
  private static final long COOLDOWN_TIME = 250;

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    ItemStack item = event.getItem();

    if (item != null && item.hasItemMeta()) {

      ItemMeta meta = item.getItemMeta();

      if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == Main.CMDGoldfishBucket) {
        if (event.getAction().isRightClick()) {

          long currentTime = System.currentTimeMillis();
          long lastPlacedTime = lastPlacedBlockTimes.getOrDefault(player, 0L);

          if (currentTime - lastPlacedTime >= COOLDOWN_TIME) {
            if (event.getClickedBlock() != null) {

              Location loc = event.getClickedBlock().getLocation();
              new Goldfish(loc.add(0, 1.3, 0));

              lastPlacedBlockTimes.put(player, currentTime);
              if (player.getGameMode() != GameMode.CREATIVE) {

                item.setType(Material.WATER_BUCKET);
                item.setItemMeta(null);

              } else {

                loc.getBlock().setType(Material.WATER);

              }
              event.setCancelled(true);
            }
          } else {
            event.setCancelled(true);
          }
        }
      }
    }
  }
}
