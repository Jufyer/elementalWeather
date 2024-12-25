package org.jufyer.plugin.aquatic.oceanGlider.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jufyer.plugin.aquatic.Main;

public class OceanGliderItem {

  public OceanGliderItem() {
    ItemStack oceanGlider;
    oceanGlider = new ItemStack(Material.BAMBOO_RAFT);

    ItemMeta meta = oceanGlider.getItemMeta();
    meta.setCustomModelData(Main.CMDOceanGlider);
    meta.setDisplayName("§rOcean Glider");
    oceanGlider.setItemMeta(meta);
  }
}
