package org.jufyer.plugin.aquatic.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.aquatic.Main;

public class GiveOceanGlider implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    Player player = (Player) sender;
    if (player.isOp()){
      ItemStack oceanGlider;
      oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);

      ItemMeta meta = oceanGlider.getItemMeta();
      meta.setCustomModelData(Main.CMDOceanGlider);
      meta.setDisplayName("§rOcean Glider");
      oceanGlider.setItemMeta(meta);

      player.getInventory().addItem(oceanGlider);
    }
    return false;
  }
}
