package org.jufyer.plugin.aquatic.nibblers.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.aquatic.Main;


public class Nibbler extends Cod {

  public static final NamespacedKey KEY = new NamespacedKey(Main.getInstance(), "Nibbler");

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
