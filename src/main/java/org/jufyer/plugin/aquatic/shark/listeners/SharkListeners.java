package org.jufyer.plugin.aquatic.shark.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jufyer.plugin.aquatic.Main;
import org.jufyer.plugin.aquatic.shark.entity.Shark;

import java.util.HashMap;
import java.util.Map;

public class SharkListeners implements Listener {
  Map<Entity, Double> TravelledDistanceOfShark = new HashMap<>();

  @EventHandler
  public void onEntityMove(EntityMoveEvent event) {
    Entity entity = event.getEntity();
    if (entity.getName().equals("Shark") && entity.getType() == EntityType.DOLPHIN && entity.getPersistentDataContainer().getKeys().contains(Shark.SHARK_KEY)){
      double distance = event.getFrom().distance(event.getTo());
      if (TravelledDistanceOfShark.get(entity) == null){
        TravelledDistanceOfShark.put(entity, 0.0);
      }
      double currentDistance = TravelledDistanceOfShark.get(entity);

      if (currentDistance >= 300) {
        ItemStack sharkTooth = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta meta = sharkTooth.getItemMeta();
        meta.setDisplayName("§rShark Tooth");
        meta.setCustomModelData(Main.CMDSharkTooth);
        sharkTooth.setItemMeta(meta);

        entity.getWorld().dropItemNaturally(entity.getLocation(), sharkTooth);

        TravelledDistanceOfShark.remove(entity);
        return;
      }

      distance += currentDistance;

      TravelledDistanceOfShark.remove(entity);
      TravelledDistanceOfShark.put(entity, distance);
    }
  }

  @EventHandler
  public void onEntityPickupItem(EntityPickupItemEvent event) {
    if (event.getEntity().getPersistentDataContainer().has(Shark.SHARK_KEY)) {
      event.setCancelled(true);
    }
  }
}
