package org.jufyer.plugin.aquatic.debug.commands;

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

import static org.jufyer.plugin.aquatic.Main.CMDOysterWithPearl;
import static org.jufyer.plugin.aquatic.oyster.listener.OysterListeners.OYSTER_PEARL_KEY;

public class spawnOyster implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender.isOp()){
      if (!(sender instanceof Player)) {
        sender.sendMessage("Only players can use this command!");
        return true;
      }

      Player player = (Player) sender;
      Location location = player.getLocation();

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
      player.sendMessage("Oyster spawned!");
    }

    return false;
  }
}
