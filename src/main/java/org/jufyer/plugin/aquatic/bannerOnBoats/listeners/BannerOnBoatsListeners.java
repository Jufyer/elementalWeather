package org.jufyer.plugin.aquatic.bannerOnBoats.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.aquatic.Main;

public class BannerOnBoatsListeners implements Listener {
  public static final NamespacedKey BANNER_ARMORSTAND_KEY = new NamespacedKey(Main.getInstance(), "BANNER_ARMORSTAND");
  public static final NamespacedKey TEMPORARY_BANNER_ARMORSTAND_KEY = new NamespacedKey(Main.getInstance(), "TEMPORARY_BANNER_ARMORSTAND");

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    Player player = event.getPlayer();
    Entity entity = event.getRightClicked();

    if (entity instanceof Boat) {
      if (player.getItemInHand().getType().name().endsWith("_BANNER")) {
        if (entity.getPassengers().toArray().length < 2) {
          ItemStack banner = player.getItemInHand();

          ArmorStand as = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
          as.setCanMove(false);
          as.setVisible(false);
          as.setGravity(false);
          as.setBasePlate(false);
          as.setArms(false);
          as.setPersistent(true);

          as.setHelmet(banner);

          as.getPersistentDataContainer().set(BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

          ArmorStand tempAs = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
          tempAs.setCanMove(false);
          tempAs.setVisible(false);
          tempAs.setGravity(false);
          tempAs.setBasePlate(false);
          tempAs.setArms(false);
          tempAs.setPersistent(true);

          tempAs.getPersistentDataContainer().set(TEMPORARY_BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

          entity.addPassenger(tempAs);
          entity.addPassenger(as);

          tempAs.setRotation(entity.getYaw(), entity.getPitch());
          as.setRotation(entity.getYaw(), entity.getPitch());
        }
      } else if (!entity.getPassengers().isEmpty()) {
        if (entity.getPassengers().get(0) instanceof ArmorStand) {
          Entity passenger = entity.getPassengers().get(0);
          if (passenger.getPersistentDataContainer().has(TEMPORARY_BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE)) {
            passenger.remove();
            entity.addPassenger(player);
          }
        }
      }
    }
  }

  @EventHandler
  public void onPlayerExitVehicle(VehicleExitEvent event) {
    if (event.getExited() instanceof Player) {
      if (event.getVehicle() instanceof Boat boat) {
        for (Entity passenger : boat.getPassengers()) {
          if (passenger instanceof ArmorStand armorStand &&
            armorStand.getPersistentDataContainer().has(BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE)) {
            ArmorStand tempAs = (ArmorStand) boat.getWorld().spawnEntity(boat.getLocation(), EntityType.ARMOR_STAND);
            tempAs.setCanMove(false);
            tempAs.setVisible(false);
            tempAs.setGravity(false);
            tempAs.setBasePlate(false);
            tempAs.setArms(false);
            tempAs.setPersistent(true);

            tempAs.getPersistentDataContainer().set(TEMPORARY_BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

            ArmorStand bannerArmorStand = (CraftArmorStand) boat.getPassengers().getLast();
            ItemStack banner = bannerArmorStand.getHelmet();

            ArmorStand as = (ArmorStand) boat.getWorld().spawnEntity(boat.getLocation(), EntityType.ARMOR_STAND);
            as.setCanMove(false);
            as.setVisible(false);
            as.setGravity(false);
            as.setBasePlate(false);
            as.setArms(false);
            as.setPersistent(true);
            as.setHelmet(banner);
            as.getPersistentDataContainer().set(BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

            boat.getPassengers().getLast().remove();


            boat.addPassenger(tempAs);
            boat.addPassenger(as);

            tempAs.setRotation(boat.getYaw(), boat.getPitch());
            as.setRotation(boat.getYaw(), boat.getPitch());
            break;
          }
        }
      }
    }
  }
}
