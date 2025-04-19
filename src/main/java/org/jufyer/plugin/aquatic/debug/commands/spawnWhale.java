package org.jufyer.plugin.aquatic.debug.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.aquatic.whale.entity.Whale;

public class spawnWhale implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender.isOp()){
      if (!(sender instanceof Player)) {
        sender.sendMessage("Only players can use this command!");
        return true;
      }

      Player player = (Player) sender;
      new Whale(player.getLocation());
      player.sendMessage("Whale spawned!");
    }
    return false;
  }
}
