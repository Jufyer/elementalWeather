package org.jufyer.plugin.aquatic.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.aquatic.Main;

public class SpawnOceanGlider implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    Player player = (Player) sender;
    if (player.isOp()){
      ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0, 2, 0), EntityType.ARMOR_STAND);
      armorStand.setCustomName("OceanGlider");
      armorStand.setPersistent(true);
      armorStand.setCanMove(false);

      armorStand.setVisible(false);

      armorStand.setGravity(false);
      armorStand.setBasePlate(false);
      armorStand.setArms(false);
      armorStand.setRotation(player.getYaw(), player.getPitch());

      ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
      ItemMeta meta = oceanGlider.getItemMeta();
      meta.setCustomModelData(Main.CMDOceanGliderEntity);
      meta.setDisplayName("§rOcean Glider");
      oceanGlider.setItemMeta(meta);

      armorStand.setItemInHand(oceanGlider);
    }
    return false;
  }
}
