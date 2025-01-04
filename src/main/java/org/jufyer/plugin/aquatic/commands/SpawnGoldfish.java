package org.jufyer.plugin.aquatic.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.aquatic.goldfish.entity.Goldfish;

public class SpawnGoldfish implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    new Goldfish(player.getLocation());
    return false;
  }
}
