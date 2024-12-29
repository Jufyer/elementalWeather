package org.jufyer.plugin.aquatic.nibblers.listeners;

import com.destroystokyo.paper.entity.PaperPathfinder;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.aquatic.Main;
import org.jufyer.plugin.aquatic.nibblers.entity.Nibbler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NibblerListeners implements Listener {

  @EventHandler
  public void onPlayerBucketEntity(PlayerBucketEntityEvent event) {
    Entity entity = event.getEntity();
    ItemStack newBucket = event.getEntityBucket();
    if (entity.getType() == EntityType.COD && entity.getName().equals("Nibbler")){
      ItemStack Nibbler_Bucket = new ItemStack(Material.COD_BUCKET);
      ItemMeta meta = Nibbler_Bucket.getItemMeta();
      meta.setCustomModelData(Main.CMDNibblerBucket);
      meta.setDisplayName("§rBucket of Nibbler");
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

      if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == Main.CMDNibblerBucket) {
        if (event.getAction().isRightClick()) {

          long currentTime = System.currentTimeMillis();
          long lastPlacedTime = lastPlacedBlockTimes.getOrDefault(player, 0L);

          if (currentTime - lastPlacedTime >= COOLDOWN_TIME) {
            if (event.getClickedBlock() != null) {

              Location loc = event.getClickedBlock().getLocation();
              new Nibbler(loc.add(0, 1.3, 0));

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

  private List<Entity> areInLove = new ArrayList<>();

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    Player player = event.getPlayer();
    Entity entity = event.getRightClicked();
    if (entity.getType() == EntityType.COD && entity.getName().equals("Nibbler")) {
      if (player.getItemInHand().getType() == Material.DRIED_KELP) {
        Location loc = entity.getLocation();
        player.getWorld().spawnParticle(Particle.HEART, loc, 30, 0.1, 0.1, 0.1);
        areInLove.add(entity);

        List<Entity> nearbyEntities = player.getNearbyEntities(15, 15, 15);
        for (Entity otherEntity : nearbyEntities) {
          if (areInLove.contains(otherEntity)) {
            Location midpoint = entity.getLocation().clone().add(otherEntity.getLocation()).multiply(0.5);
            moveEntityTowards(entity, midpoint);
            moveEntityTowards(otherEntity, midpoint);
            break;
          }
        }
      }
    }
  }
  private void moveEntityTowards(Entity entity, Location target) {
    if (!Double.isFinite(target.getX()) || !Double.isFinite(target.getY()) || !Double.isFinite(target.getZ())) {
      return;
    }
    Vector direction = target.toVector().subtract(entity.getLocation().toVector());
    if (direction.lengthSquared() == 0) {
      return;
    }
    entity.setVelocity(direction.normalize().multiply(1.15));
  }

  private final Map<Player, Long> lastHitTimes = new HashMap<>();
  private static final long HIT_COOLDOWN_TIME = 1000;

  @EventHandler
  public void onEntityMove(EntityMoveEvent event) {
    Entity entity = event.getEntity();
    if (areInLove.contains(entity)){
      List<Entity> nearbyEntities = entity.getNearbyEntities(0.5, 0.5, 0.5);
      for (Entity otherEntity : nearbyEntities) {
        if (areInLove.contains(otherEntity)) {
          Location midpoint = entity.getLocation().clone().add(otherEntity.getLocation()).multiply(0.5);
          new Nibbler(midpoint);
          areInLove.remove(entity);
          areInLove.remove(otherEntity);

          break;
        }
      }
    } else if (entity.getName().equals("Nibbler")) {
      List<Entity> nearbyEntities = entity.getNearbyEntities(20, 20, 20);
      for (Entity player : nearbyEntities) {
        if (player instanceof Player){
          Location loc = player.getLocation();
          moveEntityTowards(player, loc);

          long currentTime = System.currentTimeMillis();
          long lastHitTime = lastHitTimes.getOrDefault(player, 0L);

          if (currentTime - lastHitTime >= HIT_COOLDOWN_TIME) {
            lastPlacedBlockTimes.put((Player) player, currentTime);
            List<Entity> nearbyEntities2 = entity.getNearbyEntities(0.5, 0.5, 0.5);
            for (Entity otherEntity : nearbyEntities2) {
              ((Player) player).damage(3);
              moveEntityTowards(otherEntity, player.getLocation());
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    if (event.getEntity().getType() == EntityType.COD && event.getEntity().getName().equals("Nibbler")) {
      for (ItemStack drop : event.getDrops()){
        drop.setAmount(0);
      }
      event.getEntity().getLocation().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.PRISMARINE_SHARD));
    }
  }
}
