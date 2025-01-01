package org.jufyer.plugin.aquatic.oyster.listener;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jufyer.plugin.aquatic.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.jufyer.plugin.aquatic.Main.*;

public class OysterListeners implements Listener {

  public static final NamespacedKey OYSTER_PEARL_KEY = new NamespacedKey(Main.getInstance(), "OYSTER_PEARL");
  public static final NamespacedKey OYSTER_KEY = new NamespacedKey(Main.getInstance(), "OYSTER");
  public static final NamespacedKey OYSTER_USED_KEY = new NamespacedKey(Main.getInstance(), "OYSTER_USED");
  public static final NamespacedKey RAW_OYSTER_KEY = new NamespacedKey(Main.getInstance(), "RAW_OYSTER");
  public static final NamespacedKey COOKED_OYSTER_KEY = new NamespacedKey(Main.getInstance(), "COOKED_OYSTER");


  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    if (!event.isNewChunk()) return;

    Chunk chunk = event.getChunk();
    World world = chunk.getWorld();

    Random random = new Random();

    if (random.nextInt(100) < 10) {
      Block waterBlock = null;
      for (int x = 0; x < 16; x++) {
        for (int z = 0; z < 16; z++) {
          int worldX = (chunk.getX() << 4) + x;
          int worldZ = (chunk.getZ() << 4) + z;
          waterBlock = getOceanFloorWaterBlock(world, worldX, worldZ);
          if (waterBlock != null) {
            break;
          }
        }
        if (waterBlock != null) {
          break;
        }
      }

      if (waterBlock != null) {
        Location location = waterBlock.getLocation().add(0.5, 0, 0.5);
        if (random.nextBoolean()) {
          spawnOysterWithoutPearl(location);
        } else {
          spawnOysterWithPearl(location);
        }
      }
    }
  }

  private Block getOceanFloorWaterBlock(World world, int x, int z) {
    for (int y = world.getMaxHeight() - 1; y > world.getMinHeight(); y--) {
      Block block = world.getBlockAt(x, y, z);
      if (block.getType() == Material.WATER) {
        Block below = block.getRelative(0, -1, 0);
        if (below.getType().isSolid()) {
          return block;
        }
      }
    }
    return null;
  }

  public void spawnOysterWithoutPearl(Location location) {
    ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    armorStand.setCustomName("Oyster");
    armorStand.setPersistent(true);
    armorStand.setCanMove(false);
    armorStand.getPersistentDataContainer().set(OYSTER_KEY, PersistentDataType.BYTE, (byte) 1);
    armorStand.setVisible(false);

    armorStand.setGravity(false);
    armorStand.setBasePlate(false);
    armorStand.setArms(false);
    armorStand.setSmall(true);

    ItemStack Osyter = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = Osyter.getItemMeta();
    meta.setCustomModelData(CMDOyster);
    meta.setDisplayName("§rOyster");
    Osyter.setItemMeta(meta);

    armorStand.setHelmet(Osyter);
  }

  public void spawnOysterWithPearl(Location location) {
    ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    as.setCustomName("Oyster");
    as.setPersistent(true);
    as.getPersistentDataContainer().set(OYSTER_PEARL_KEY, PersistentDataType.BYTE, (byte) 1);
    as.setCanMove(false);

    as.setVisible(false);

    as.setGravity(false);
    as.setBasePlate(false);
    as.setArms(false);
    as.setSmall(true);

    ItemStack OysterWithPearl = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta OsyterWithPearlmeta = OysterWithPearl.getItemMeta();
    OsyterWithPearlmeta.setCustomModelData(CMDOysterWithPearl);
    OsyterWithPearlmeta.setDisplayName("§rOyster with Pearl");
    OysterWithPearl.setItemMeta(OsyterWithPearlmeta);

    as.setHelmet(OysterWithPearl);
  }

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    Entity entity = event.getRightClicked();
    if (!(entity instanceof ArmorStand)) return;

    ArmorStand armorStand = (ArmorStand) entity;
    if (armorStand.getPersistentDataContainer().has(OYSTER_KEY, PersistentDataType.BYTE) ||
      armorStand.getPersistentDataContainer().has(OYSTER_PEARL_KEY, PersistentDataType.BYTE)) {
      if (!armorStand.getPersistentDataContainer().has(OYSTER_USED_KEY, PersistentDataType.BYTE)){
        Location location = armorStand.getLocation();

        org.bukkit.inventory.ItemStack raw_oyster = new org.bukkit.inventory.ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta meta = raw_oyster.getItemMeta();
        meta.setCustomModelData(CMDRawOyster);
        meta.setDisplayName("§rRaw Oyster");
        meta.getPersistentDataContainer().set(RAW_OYSTER_KEY, PersistentDataType.BYTE, (byte) 1);
        raw_oyster.setItemMeta(meta);


        ItemStack pearl = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta pearlMeta = pearl.getItemMeta();
        pearlMeta.setCustomModelData(Main.CMDPearl);
        pearlMeta.setDisplayName("§rPearl");
        pearl.setItemMeta(pearlMeta);

        location.getWorld().dropItemNaturally(location, raw_oyster);

        if (armorStand.getPersistentDataContainer().has(OYSTER_PEARL_KEY, PersistentDataType.BYTE)){
          location.getWorld().dropItemNaturally(location, pearl);
        }

        ItemStack DeadOsyter = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta DeadOystermeta = DeadOsyter.getItemMeta();
        DeadOystermeta.setCustomModelData(CMDDeadOyster);
        DeadOystermeta.setDisplayName("§rDead Oyster");
        DeadOsyter.setItemMeta(DeadOystermeta);

        armorStand.setHelmet(DeadOsyter);

        armorStand.getPersistentDataContainer().set(OYSTER_USED_KEY, PersistentDataType.BYTE, (byte) 2);
      }
    }
  }

  @EventHandler
  public void onPlayerConsumeRawOyster(PlayerItemConsumeEvent event) {
    ItemStack consumedItem = event.getItem();
    if (consumedItem.hasItemMeta() &&
      consumedItem.getItemMeta().getPersistentDataContainer().has(RAW_OYSTER_KEY, PersistentDataType.BYTE)) {

      event.getPlayer().setFoodLevel(event.getPlayer().getFoodLevel() + 2);
      event.getPlayer().setSaturation(event.getPlayer().getSaturation() + 2.0f);
      event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 200, 3));

      event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), "entity.player.burp", 1.0f, 1.0f);
    }
  }

  private final Map<Player, Long> lastEatenTimes = new HashMap<>();
  private static final long COOLDOWN_TIME = 250;

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    ItemStack hand = player.getItemInHand();

    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }

    if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    if (event.getClickedBlock() != null && isInteractable(event.getClickedBlock().getType())) {
      return;
    }

    if (hand.hasItemMeta() && hand.getItemMeta().hasCustomModelData() && hand.getItemMeta().getCustomModelData() == CMDRawOyster) {
      long currentTime = System.currentTimeMillis();

      long lastPlacedTime = lastEatenTimes.getOrDefault(player, 0L);

      if (currentTime - lastPlacedTime >= COOLDOWN_TIME) {
        onPlayerConsumeRawOyster(new PlayerItemConsumeEvent(player, hand, event.getHand()));
        if (player.getGameMode().equals(GameMode.CREATIVE)){
          return;
        }
        hand.setAmount(hand.getAmount() -1);

      }else {
          event.setCancelled(true);
        }

    }
  }

  private boolean isInteractable(Material material) {
    return material == Material.CRAFTING_TABLE ||
      material == Material.CHEST ||
      material == Material.FURNACE ||
      material == Material.ANVIL ||
      material == Material.BARREL ||
      material == Material.BREWING_STAND ||
      material == Material.ENCHANTING_TABLE ||
      material == Material.HOPPER ||
      material == Material.DISPENSER ||
      material == Material.DROPPER ||
      material == Material.LECTERN ||
      material == Material.STONECUTTER ||
      material == Material.GRINDSTONE ||
      material == Material.LOOM ||
      material == Material.BLAST_FURNACE ||
      material == Material.SMOKER ||
      material == Material.CARTOGRAPHY_TABLE ||
      material == Material.COMPOSTER ||
      material == Material.NOTE_BLOCK ||
      material == Material.JUKEBOX ||
      material == Material.TRAPPED_CHEST ||
      material == Material.OAK_DOOR ||
      material == Material.CHISELED_BOOKSHELF ||
      material == Material.REPEATER ||
      material == Material.COMPARATOR ||
      material == Material.DECORATED_POT ||
      material.name().endsWith("_DOOR") ||
      material.name().endsWith("_TRAPDOOR") ||
      material.name().endsWith("_PRESSURE_PLATE") ||
      material.name().endsWith("_FENCE") ||
      material.name().endsWith("_FENCE_GATE") ||
      material.name().endsWith("_BUTTON") ||
      material.name().endsWith("_SIGN") ||
      material.name().endsWith("_MINECART") ||
      material.name().endsWith("_COMMAND_BLOCK") ||
      material.name().endsWith("_BOAT");
  }
}
