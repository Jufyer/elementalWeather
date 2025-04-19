package org.jufyer.plugin.aquatic.oyster.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jufyer.plugin.aquatic.Main;

public class PotionOfLuckListeners implements Listener {

  @EventHandler
  public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
    ItemStack item = event.getItem();
    if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == Main.CMDPotionOfLuck){
      event.getPlayer().sendMessage("potion getrunken");
    }
  }
}
