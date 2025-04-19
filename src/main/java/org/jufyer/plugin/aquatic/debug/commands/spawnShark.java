package org.jufyer.plugin.aquatic.debug.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.aquatic.shark.entity.Shark;

public class spawnShark implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (sender.isOp()){
      if (!(sender instanceof Player)) {
        sender.sendMessage("Only players can use this command!");
        return true;
      }

      Player player = (Player) sender;
      new Shark(player.getLocation());
      player.sendMessage("Shark spawned!");
    }
    return false;
  }
}
