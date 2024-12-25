package org.jufyer.plugin.aquatic.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.aquatic.oceanGlider.entity.OceanGlider;

public class SpawnOceanGlider implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    Player player = (Player) sender;
    new OceanGlider().spawnOceanGlider(player.getLocation());
    return false;
  }
}
