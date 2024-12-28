package org.jufyer.plugin.aquatic.nibblers.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.aquatic.Main;


public class Nibbler extends Cod {

  public static final NamespacedKey KEY = new NamespacedKey(Main.getInstance(), "Whale");

  public Nibbler(Location loc) {
    super(EntityType.COD, ((CraftWorld) loc.getWorld()).getHandle());

    this.setPosRaw(loc.getX(), loc.getY(), loc.getZ());
    this.getBukkitEntity().getPersistentDataContainer().set(KEY, PersistentDataType.BOOLEAN, true);
    this.setInvulnerable(true);
    this.setCustomName(Component.nullToEmpty("Nibbler"));
    this.setCustomNameVisible(false);

    this.persist = true;

    ((CraftWorld) loc.getWorld()).getHandle().addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
  }
}
