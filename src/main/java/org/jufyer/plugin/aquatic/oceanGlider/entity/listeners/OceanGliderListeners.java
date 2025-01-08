package org.jufyer.plugin.aquatic.oceanGlider.entity.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
  private Map<ArmorStand, Location> LocationBeforeArmorStandMap = new HashMap<>();
  private List<Player> areAllowedToFly = new ArrayList<>();
  private Map<ArmorStand, Double> DistanceFromArmorStand = new HashMap<>();

  @EventHandler
  public void onPlayerJump(PlayerJumpEvent event) {
    Player player = event.getPlayer();
    if (ridingPlayers.contains(player)){
      if (areAllowedToFly.contains(player)) {
        return;
      }
      player.setAllowFlight(false);
    }
  }

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
      ArmorStand as = (ArmorStand) event.getRightClicked();
      if (as.getEquipment().getItemInMainHand() != null && as.getEquipment().getItemInMainHand().getType() == Material.NAUTILUS_SHELL) {
        if (as.getEquipment().getItemInMainHand().hasItemMeta() &&
          as.getEquipment().getItemInMainHand().getItemMeta().hasCustomModelData() &&
          as.getEquipment().getItemInMainHand().getItemMeta().getCustomModelData() == Main.CMDOceanGliderEntity) {

          ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
          ItemMeta meta = oceanGlider.getItemMeta();
          meta.setCustomModelData(Main.CMDOceanGliderEntity);
          meta.setDisplayName("§rOcean Glider");
          oceanGlider.setItemMeta(meta);

          ItemStack air = new ItemStack(Material.AIR);

          as.setItemInHand(air);
          as.setHelmet(oceanGlider);

          Player player = event.getPlayer();
          double x = as.getLocation().getX();
          double y = as.getWorld().getHighestBlockYAt(as.getLocation()) + 0.81;
          double z = as.getLocation().getZ();

          LocationBeforeArmorStandMap.put(as, as.getLocation());

          player.teleport(new Location(player.getWorld(), x, y, z, player.getYaw(), player.getPitch()));
          player.setGravity(false);

          player.addPassenger(as);

          if (!(areAllowedToFly.contains(event.getPlayer())) && player.getAllowFlight()) {
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
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player){
      Player player = (Player) event.getDamager();
      Entity entity = event.getEntity();
      if (entity instanceof ArmorStand){
        ArmorStand as = (ArmorStand) event.getEntity();
        if (as.getEquipment().getItemInHand() != null && as.getEquipment().getItemInHand().getType() == Material.NAUTILUS_SHELL ||
          as.getEquipment().getHelmet() != null && as.getEquipment().getHelmet().getType() == Material.NAUTILUS_SHELL) {
          if (as.getEquipment().getItemInHand().hasItemMeta() &&
            as.getEquipment().getItemInHand().getItemMeta().hasCustomModelData() &&
            as.getEquipment().getItemInHand().getItemMeta().getCustomModelData() == Main.CMDOceanGliderEntity ||
            as.getEquipment().getHelmet().hasItemMeta() &&
            as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() &&
            as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDOceanGliderEntity) {

            ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
            ItemMeta meta = oceanGlider.getItemMeta();
            meta.setCustomModelData(Main.CMDOceanGlider);
            meta.setDisplayName("§rOcean Glider");
            oceanGlider.setItemMeta(meta);
            player.getWorld().dropItemNaturally(as.getLocation(), oceanGlider);

            as.remove();
          }
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
            player.setFlySpeed(0.1f);
            player.setGravity(true);

            float yaw = player.getYaw();
            float pitch = player.getPitch();

            player.removePassenger(as);

            as.setHelmet(new ItemStack(Material.AIR));

            ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
            ItemMeta meta = oceanGlider.getItemMeta();
            meta.setCustomModelData(Main.CMDOceanGliderEntity);
            meta.setDisplayName("§rOcean Glider");
            oceanGlider.setItemMeta(meta);
            as.setItemInHand(oceanGlider);

            Location location = LocationBeforeArmorStandMap.get(as);
            as.teleport(location.add(0, 0.2, 0));
            as.setRotation(yaw, pitch);

            ridingPlayers.remove(event.getPlayer());
            playerArmorStandMap.remove(event.getPlayer(), as);
            return;
          }
          event.setCancelled(true);
          return;
        }
      }
      ArmorStand as = playerArmorStandMap.get(player);

      player.setAllowFlight(true);
      player.setFlying(true);
      player.setFlySpeed(0.2f);

      if (as != null){
        as.setRotation(player.getYaw(), player.getPitch());
        LocationBeforeArmorStandMap.put(as, player.getLocation());
      }

      double fromY = event.getFrom().getY();
      double toY = event.getTo().getY();

      if (fromY != toY) {
        Location newLocation = new Location(event.getTo().getWorld(), event.getTo().getX(), fromY, event.getTo().getZ());
        player.teleport(newLocation);
      }

      double distance = event.getFrom().distance(event.getTo());
      double currentDistance;
      if (DistanceFromArmorStand.get(as) != null){
        currentDistance = DistanceFromArmorStand.get(as);
      }else {
        currentDistance = 0;
      }

      if (currentDistance >= 300) {
        as.remove();
        if (ridingPlayers.contains(player) && playerArmorStandMap.containsKey(player)) {
          player.setFlying(false);
          if (!(player.isOp())){
            player.setAllowFlight(false);
          }
          player.setFlySpeed(0.1f);
          player.setGravity(true);

          float yaw = player.getYaw();
          float pitch = player.getPitch();

          player.removePassenger(as);

          as.setHelmet(new ItemStack(Material.AIR));

          ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
          ItemMeta meta = oceanGlider.getItemMeta();
          meta.setCustomModelData(Main.CMDOceanGliderEntity);
          meta.setDisplayName("§rOcean Glider");
          oceanGlider.setItemMeta(meta);
          as.setItemInHand(oceanGlider);

          Location location = LocationBeforeArmorStandMap.get(as);
          as.teleport(location);
          as.setRotation(yaw, pitch);

          ridingPlayers.remove(event.getPlayer());
          playerArmorStandMap.remove(event.getPlayer(), as);
        }
        return;
      }

      distance += currentDistance;

      DistanceFromArmorStand.remove(as);
      DistanceFromArmorStand.put(as, distance);
    }
  }

  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();

    if (ridingPlayers.contains(player) && playerArmorStandMap.containsKey(player)) {
      ArmorStand as = playerArmorStandMap.get(player);
      player.setFlying(false);
      if (!(player.isOp())){
        player.setAllowFlight(false);
      }
      player.setFlySpeed(0.1f);
      player.setGravity(true);

      float yaw = player.getYaw();
      float pitch = player.getPitch();

      player.removePassenger(as);

      as.setHelmet(new ItemStack(Material.AIR));

      ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
      ItemMeta meta = oceanGlider.getItemMeta();
      meta.setCustomModelData(Main.CMDOceanGliderEntity);
      meta.setDisplayName("§rOcean Glider");
      oceanGlider.setItemMeta(meta);
      as.setItemInHand(oceanGlider);

      Location location = LocationBeforeArmorStandMap.get(as);
      as.teleport(location.add(0, 0.2, 0));
      as.setRotation(yaw, pitch);

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
                Location loc = rayTraceResult.getHitBlock().getLocation().add(0, /*0.7*/-1, 0);

                if (loc.getBlock().isLiquid()){
                  spawnOceanGlider(loc, player.getYaw(), player.getPitch());

                  lastPlacedBlockTimes.put(player, currentTime);
                }
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

  public void spawnOceanGlider(Location location, float yaw, float pitch) {
    ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 2, 0), EntityType.ARMOR_STAND);
    armorStand.setCustomName("OceanGlider");
    armorStand.setPersistent(true);
    armorStand.setCanMove(false);

    armorStand.setVisible(false);

    armorStand.setGravity(false);
    armorStand.setBasePlate(false);
    armorStand.setArms(false);
    armorStand.setRotation(yaw, pitch);

    ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = oceanGlider.getItemMeta();
    meta.setCustomModelData(Main.CMDOceanGliderEntity);
    meta.setDisplayName("§rOcean Glider");
    oceanGlider.setItemMeta(meta);

    armorStand.setItemInHand(oceanGlider);
    DistanceFromArmorStand.put(armorStand, 0.0);
  }
}
