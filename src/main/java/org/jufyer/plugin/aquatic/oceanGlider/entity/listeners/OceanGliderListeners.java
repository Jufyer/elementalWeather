package org.jufyer.plugin.aquatic.oceanGlider.entity.listeners;

import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jufyer.plugin.aquatic.Main;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OceanGliderListeners implements Listener {
  private List<Player> ridingPlayers = new ArrayList<>();
  private Map<Player, ArmorStand> playerArmorStandMap = new HashMap<>();
  private List<Player> areAllowedToFly = new ArrayList<>();

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
      ArmorStand as = (ArmorStand) event.getRightClicked();
      if (as.getEquipment().getHelmet() != null && as.getEquipment().getHelmet().getType() == Material.NAUTILUS_SHELL) {
        if (as.getEquipment().getHelmet().hasItemMeta() &&
          as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() &&
          as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDOceanGliderEntity) {

          Player player = event.getPlayer();
          double x = as.getLocation().getX();
          double y = as.getLocation().getY() -1.975;
          double z = as.getLocation().getZ();
          player.teleport(new Location(player.getWorld(), x, y, z, player.getYaw(), player.getPitch()));
          player.setGravity(false);

          player.addPassenger(as);

          if (!(areAllowedToFly.contains(event.getPlayer()))) {
            areAllowedToFly.add(event.getPlayer());
          }
          ridingPlayers.add(event.getPlayer());
          playerArmorStandMap.put(event.getPlayer(), as);

          event.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    if (ridingPlayers.contains(player)) {
      if (player.isFlying()) {

        double previousY = event.getFrom().getY();
        double currentY = event.getTo().getY();

        if (currentY > previousY) {
          if (ridingPlayers.contains(player) && playerArmorStandMap.containsKey(player)) {
            ArmorStand as = playerArmorStandMap.get(player);

            if (!(areAllowedToFly.contains(player))){
              player.setAllowFlight(false);
            }
            player.setFlying(false);
            player.setFlySpeed(1f);
            player.setGravity(true);

            player.removePassenger(as);

            ridingPlayers.remove(event.getPlayer());
            playerArmorStandMap.remove(event.getPlayer(), as);
          }
          event.setCancelled(true);
          return;
        }
      }
      ArmorStand as = playerArmorStandMap.get(player);

      player.setAllowFlight(true);
      player.setFlying(true);
      player.setFlySpeed(0.1f);

      as.setRotation(player.getYaw(), player.getPitch());

      double fromY = event.getFrom().getY();
      double toY = event.getTo().getY();

      if (fromY != toY) {
        Location newLocation = new Location(event.getTo().getWorld(), event.getTo().getX(), fromY, event.getTo().getZ());
        player.teleport(newLocation);
      }
    }
  }

  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();

    if (ridingPlayers.contains(player) && playerArmorStandMap.containsKey(player)) {
      ArmorStand as = playerArmorStandMap.get(player);

      if (!(player.isOp())){
        player.setAllowFlight(false);
      }
      player.setFlying(false);
      player.setFlySpeed(0.1f);
      player.setGravity(true);

      Location loc = as.getLocation();

      player.removePassenger(as);
      as.teleport(loc);

      ridingPlayers.remove(event.getPlayer());
      playerArmorStandMap.remove(event.getPlayer(), as);
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
      if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == Main.CMDOceanGlider) {
        if (event.getAction().isRightClick()) {
          long currentTime = System.currentTimeMillis();
          long lastPlacedTime = lastPlacedBlockTimes.getOrDefault(player, 0L);

          if (currentTime - lastPlacedTime >= COOLDOWN_TIME) {
            RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(
              player.getEyeLocation(),
              player.getEyeLocation().getDirection(),
              5,
              FluidCollisionMode.SOURCE_ONLY
            );

            if (rayTraceResult != null && rayTraceResult.getHitBlock() != null) {
              Material clickedBlockType = rayTraceResult.getHitBlock().getType();

              if (clickedBlockType == Material.WATER) {
                Location loc = rayTraceResult.getHitBlock().getLocation().add(0, 0.7, 0);
                spawnOceanGlider(loc);
                lastPlacedBlockTimes.put(player, currentTime);
                if (player.getGameMode() != GameMode.CREATIVE) {
                  item.setAmount(item.getAmount() - 1);
                }

              }
            }
          } else {
            event.setCancelled(true);
          }
        }
      }
    }
  }


  public void spawnOceanGlider(Location location) {
    ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 2, 0), EntityType.ARMOR_STAND);
    armorStand.setCustomName("OceanGlider");
    armorStand.setPersistent(true);
    armorStand.setCanMove(false);
    armorStand.setVisible(true);
    armorStand.setGravity(false);
    armorStand.setBasePlate(false);
    armorStand.setArms(false);

    ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = oceanGlider.getItemMeta();
    meta.setCustomModelData(Main.CMDOceanGliderEntity);
    meta.setDisplayName("§rOcean Glider");
    oceanGlider.setItemMeta(meta);

    armorStand.setHelmet(oceanGlider);
  }
}
