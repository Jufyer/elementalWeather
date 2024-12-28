package org.jufyer.plugin.aquatic.oceanGlider.entity.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jufyer.plugin.aquatic.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OceanGliderListeners implements Listener {
  private List<Player> ridingPlayers = new ArrayList<>();
  private Map<Player, ArmorStand> playerArmorStandMap = new HashMap<>();

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
      ArmorStand as = (ArmorStand) event.getRightClicked();
      if (as.getEquipment().getHelmet() != null && as.getEquipment().getHelmet().getType() == Material.NAUTILUS_SHELL) {
        if (as.getEquipment().getHelmet().hasItemMeta() &&
          as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() &&
          as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDOceanGliderEntity) {
          as.teleport(new Location(as.getWorld(), as.getX(), as.getY() - 2, as.getZ(), as.getYaw(), as.getPitch()));

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
      ArmorStand armorStand = playerArmorStandMap.get(player);
      if (armorStand != null) {
        Location playerLocation = player.getLocation();

        // Verhindere vertikale Bewegung (wenn der Spieler nach oben oder unten schaut)
        double pitch = playerLocation.getPitch();
        if (pitch > -45 && pitch < 45) {
          // Bewegung in Blickrichtung des Spielers berechnen
          float yaw = playerLocation.getYaw();
          double radians = Math.toRadians(yaw);

          double x = Math.sin(radians) * 0.5; // Bewegungsgeschwindigkeit
          double z = Math.cos(radians) * -0.5; // Bewegungsgeschwindigkeit

          // Neue Position setzen
          Location newLocation = armorStand.getLocation().add(x, 0, z);
          armorStand.teleport(newLocation);

          // Spieler mit dem ArmorStand synchronisieren
          Location playerNewLocation = newLocation.clone().add(0, 2, 0);
          player.teleport(playerNewLocation);
        }
      }

      // Verhindere, dass der Spieler sich unabhängig bewegt
      event.setCancelled(true);
    }
  }
}
