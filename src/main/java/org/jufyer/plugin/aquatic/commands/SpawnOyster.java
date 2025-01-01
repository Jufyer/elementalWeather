package org.jufyer.plugin.aquatic.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.aquatic.oyster.listener.OysterListeners;

import java.util.HashMap;
import java.util.Map;

import static org.jufyer.plugin.aquatic.Main.CMDOyster;
import static org.jufyer.plugin.aquatic.Main.CMDOysterWithPearl;
import static org.jufyer.plugin.aquatic.oyster.listener.OysterListeners.OYSTER_KEY;
import static org.jufyer.plugin.aquatic.oyster.listener.OysterListeners.OYSTER_PEARL_KEY;

public class SpawnOyster implements CommandExecutor {


  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;

    spawnOysterWithoutPearl(player.getLocation());
    spawnOysterWithPearl(player.getLocation().add(3, 0, 0));

    return false;
  }

  public void spawnOysterWithoutPearl(Location location) {
    ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    armorStand.setCustomName("Oyster");
    armorStand.setPersistent(true);
    armorStand.setCanMove(false);
    armorStand.getPersistentDataContainer().set(OYSTER_KEY, PersistentDataType.BYTE, (byte) 1);
    armorStand.setVisible(false);

    armorStand.setGravity(false);
    armorStand.setBasePlate(false);
    armorStand.setArms(false);
    armorStand.setSmall(true);

    ItemStack Osyter = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = Osyter.getItemMeta();
    meta.setCustomModelData(CMDOyster);
    meta.setDisplayName("§rOyster");
    Osyter.setItemMeta(meta);

    armorStand.setHelmet(Osyter);
  }

  public void spawnOysterWithPearl(Location location) {
    ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    as.setCustomName("Oyster");
    as.setPersistent(true);
    as.getPersistentDataContainer().set(OYSTER_PEARL_KEY, PersistentDataType.BYTE, (byte) 1);
    as.setCanMove(false);

    as.setVisible(false);

    as.setGravity(false);
    as.setBasePlate(false);
    as.setArms(false);
    as.setSmall(true);

    ItemStack OysterWithPearl = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta OsyterWithPearlmeta = OysterWithPearl.getItemMeta();
    OsyterWithPearlmeta.setCustomModelData(CMDOysterWithPearl);
    OsyterWithPearlmeta.setDisplayName("§rOyster with Pearl");
    OysterWithPearl.setItemMeta(OsyterWithPearlmeta);

    as.setHelmet(OysterWithPearl);
  }
}
