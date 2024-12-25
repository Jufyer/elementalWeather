package org.jufyer.plugin.aquatic.oceanGlider.entity.listeners;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jufyer.plugin.aquatic.Main;
import org.bukkit.util.Vector;

public class OceanGliderListeners implements Listener {
  private final Main plugin;

  public OceanGliderListeners(Main plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    // Überprüfen, ob der Spieler auf einen ArmorStand klickt
    if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
      ArmorStand as = (ArmorStand) event.getRightClicked();
      // Überprüfen, ob der ArmorStand eine Nautilus Shell als Helm trägt
      if (as.getEquipment().getHelmet() != null && as.getEquipment().getHelmet().getType() == Material.NAUTILUS_SHELL) {
        // Überprüfen, ob der Helm ein benutzerdefiniertes Modell-Datenattribut hat
        if (as.getEquipment().getHelmet().hasItemMeta() &&
          as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() &&
          as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDOceanGliderEntity) {
          Boat
          // Den Spieler und den ArmorStand miteinander verbinden (Ritteffekt wie bei einem Pferd)
          Player player = event.getPlayer();
          player.setPassenger(as); // Der Spieler wird zum Passagier des ArmorStands
          as.setGravity(false); // ArmorStand soll keine Schwerkraft mehr haben
          as.setBasePlate(false); // ArmorStand ohne Basisplatte anzeigen
          as.setArms(true); // Arme des ArmorStands aktivieren
          as.setVisible(false); // ArmorStand unsichtbar machen (optional)
        }
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();

    // Überprüfen, ob der Spieler auf einem ArmorStand sitzt
    if (player.getPassenger() instanceof ArmorStand) {
      ArmorStand as = (ArmorStand) player.getPassenger();

      // Wir synchronisieren die Position des ArmorStands mit der Bewegung des Spielers
      Vector velocity = player.getVelocity();

      // Setze die Geschwindigkeit des ArmorStands auf die des Spielers (angepasst)
      as.setVelocity(velocity.multiply(0.5)); // Passe den Multiplikator für Geschwindigkeit an

      // Drehe den ArmorStand entsprechend der Blickrichtung des Spielers
      as.setRotation(player.getLocation().getYaw(), player.getLocation().getPitch());

      // Optional: weitere Anpassungen der Position oder Bewegung des ArmorStands
    }
  }
}
