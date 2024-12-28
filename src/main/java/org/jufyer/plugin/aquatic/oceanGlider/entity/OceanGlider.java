package org.jufyer.plugin.aquatic.oceanGlider.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jufyer.plugin.aquatic.Main;

public class OceanGlider {
  public void spawnOceanGlider(Location location) {
    ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    armorStand.setCustomName("OceanGlider");
    armorStand.setPersistent(true);
    armorStand.setCanMove(false);
    armorStand.setVisible(true);
    armorStand.setGravity(false);
    armorStand.setBasePlate(false);
    armorStand.setArms(false);

    ItemStack oceanGlider = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = oceanGlider.getItemMeta();
    meta.setCustomModelData(Main.CMDOceanGliderEntity);
    meta.setDisplayName("§rOcean Glider");
    oceanGlider.setItemMeta(meta);

    armorStand.setHelmet(oceanGlider);
  }
}
