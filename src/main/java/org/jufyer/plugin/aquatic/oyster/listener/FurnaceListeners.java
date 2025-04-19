package org.jufyer.plugin.aquatic.oyster.listener;

import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static org.jufyer.plugin.aquatic.Main.CMDRawOyster;
import static org.jufyer.plugin.aquatic.oyster.listener.OysterListeners.RAW_OYSTER_KEY;

public class FurnaceListeners implements Listener {
  @EventHandler
  private void furnaceCanceller(FurnaceSmeltEvent event) {


    ItemStack raw_oyster = new org.bukkit.inventory.ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta rawOysterItemMetameta = raw_oyster.getItemMeta();
    rawOysterItemMetameta.setCustomModelData(CMDRawOyster);
    rawOysterItemMetameta.setDisplayName("§rRaw Oyster");
    rawOysterItemMetameta.getPersistentDataContainer().set(RAW_OYSTER_KEY, PersistentDataType.BYTE, (byte) 1);
    raw_oyster.setItemMeta(rawOysterItemMetameta);


    if(event.getSource() != null){
      if(event.getSource().getType() == raw_oyster.getType()){
        if(!event.getSource().isSimilar(raw_oyster)) {
          event.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  private void furnaceCanceller(FurnaceBurnEvent event) {
    Furnace furnace = (Furnace) event.getBlock().getState();
    if(furnace != null){
      if(furnace.getInventory() != null){
        if(furnace.getInventory().getSmelting() != null){


          ItemStack raw_oyster = new org.bukkit.inventory.ItemStack(Material.NAUTILUS_SHELL);
          ItemMeta rawOysterItemMetameta = raw_oyster.getItemMeta();
          rawOysterItemMetameta.setCustomModelData(CMDRawOyster);
          rawOysterItemMetameta.setDisplayName("§rRaw Oyster");
          rawOysterItemMetameta.getPersistentDataContainer().set(RAW_OYSTER_KEY, PersistentDataType.BYTE, (byte) 1);
          raw_oyster.setItemMeta(rawOysterItemMetameta);


          if(furnace.getInventory().getSmelting().getType() == raw_oyster.getType()){
            if(!furnace.getInventory().getSmelting().isSimilar(raw_oyster)){
              event.setCancelled(true);
            }
          }
        }
      }
    }
  }
}
