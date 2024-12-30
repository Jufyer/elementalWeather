package org.jufyer.plugin.aquatic.spikyPiston.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jufyer.plugin.aquatic.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jufyer.plugin.aquatic.Main.CMDSpikyPistonItem;

public class SpikyPistonListeners implements Listener {
  @EventHandler
  public void onBlockPistonExtend(BlockPistonExtendEvent event) {
    List<Block> blocks = event.getBlocks();

    if (!blocks.isEmpty()) {
      Block block = blocks.get(0);

      if (block.getType().getBlastResistance() <= 6) {
        block.setType(Material.AIR);
        event.setCancelled(true);
      }
    }
  }

  private final Map<Player, Long> lastPlacedBlockTimes = new HashMap<>();
  private static final long COOLDOWN_TIME = 250;

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    ItemStack item = event.getItem();
    if (item != null && item.hasItemMeta()) {
      ItemMeta meta = item.getItemMeta();
      if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == CMDSpikyPistonItem) {

        if (event.getAction().isRightClick()) {
          Player player = event.getPlayer();
          long currentTime = System.currentTimeMillis();

          long lastPlacedTime = lastPlacedBlockTimes.getOrDefault(player, 0L);

          if (currentTime - lastPlacedTime >= COOLDOWN_TIME) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.AIR && !event.getClickedBlock().isLiquid()) {
              if (event.getBlockFace().equals(BlockFace.UP)){
                Location loc = event.getClickedBlock().getLocation().add(0, 1, 0);
                createBlock(loc, BlockFace.UP);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }else if (event.getBlockFace().equals(BlockFace.DOWN)){
                Location loc = event.getClickedBlock().getLocation().add(0, -1, 0);
                createBlock(loc, BlockFace.DOWN);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }else if (event.getBlockFace().equals(BlockFace.WEST)){
                Location loc = event.getClickedBlock().getLocation().add(-1, 0, 0);
                createBlock(loc, BlockFace.WEST);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }else if (event.getBlockFace().equals(BlockFace.EAST)){
                Location loc = event.getClickedBlock().getLocation().add(1, 0, 0);
                createBlock(loc, BlockFace.EAST);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }if (event.getBlockFace().equals(BlockFace.NORTH)){
                Location loc = event.getClickedBlock().getLocation().add(0, 0, -1);
                createBlock(loc, BlockFace.NORTH);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }else if (event.getBlockFace().equals(BlockFace.SOUTH)){
                Location loc = event.getClickedBlock().getLocation().add(0, 0, 1);
                createBlock(loc, BlockFace.SOUTH);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }
            }
          } else {
            event.setCancelled(true);
          }
        }
      }
    }
  }

  private void createBlock(Location loc, BlockFace blockFace) {
    ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = Barnacles.getItemMeta();

    if (blockFace == BlockFace.NORTH){
      meta.setCustomModelData(Main.CMDSpikyPistonNorth);
    } else if (blockFace == BlockFace.EAST) {
      meta.setCustomModelData(Main.CMDSpikyPistonEast);
    } else if (blockFace == BlockFace.SOUTH) {
      meta.setCustomModelData(Main.CMDSpikyPistonSouth);
    } else if (blockFace == BlockFace.WEST) {
      meta.setCustomModelData(Main.CMDSpikyPistonWest);
    } else if (blockFace == BlockFace.UP) {
      meta.setCustomModelData(Main.CMDSpikyPistonUp);
    } else if (blockFace == BlockFace.DOWN) {
      meta.setCustomModelData(Main.CMDSpikyPistonDown);
    }

    meta.setDisplayName("§rSpiky Piston");
    Barnacles.setItemMeta(meta);


    Location spawnLoc = loc.subtract(-0.5, 0, -0.5);
    ArmorStand as = loc.getWorld().spawn(spawnLoc, ArmorStand.class);
    as.setInvisible(true);
    as.setGravity(false);
    as.setBodyYaw(0);
    as.setRotation(0, 0);
    as.setCanMove(false);
    as.setCustomNameVisible(false);
    as.setPersistent(true);

    as.getEquipment().setHelmet(Barnacles);

    loc.getBlock().setType(Material.QUARTZ_BLOCK);
  }

  @EventHandler
  public void onBlockRedstone(BlockRedstoneEvent event) {
    Block block = event.getBlock();
    BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};

    for (BlockFace face : faces) {
      Block adjacentBlock = block.getRelative(face);
      if (event.getNewCurrent() > 0) {
        if (adjacentBlock.getType() == Material.QUARTZ_BLOCK) {
          Location blockLoc = adjacentBlock.getLocation();

          for (ArmorStand as : blockLoc.getWorld().getEntitiesByClass(ArmorStand.class)) {
            if (as.getLocation().getBlockX() == blockLoc.getBlockX() &&
              as.getLocation().getBlockY() == blockLoc.getBlockY() &&
              as.getLocation().getBlockZ() == blockLoc.getBlockZ()) {
              if (as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonNorth
                || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonEast
                || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonSouth
                || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonWest
                || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonUp
                || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonDown
              ){
                ItemMeta oldMeta = as.getEquipment().getHelmet().getItemMeta();

                as.getEquipment().getHelmet().setAmount(0);

                ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
                ItemMeta meta = Barnacles.getItemMeta();

                int CustomModelData = oldMeta.getCustomModelData();
                if (CustomModelData == Main.CMDSpikyPistonNorth) {
                  meta.setCustomModelData(Main.CMDSpikyPistonExtendedNorth);

                  Location blockToDestroy = adjacentBlock.getLocation().add(0, 0, -1);
                  if (blockToDestroy.getBlock() != null && blockToDestroy.getBlock() != BlockType.AIR){
                    blockToDestroy.getBlock().setType(Material.AIR);
                    blockToDestroy.getBlock().getLocation().getWorld().dropItemNaturally(blockToDestroy.getBlock().getLocation(), new ItemStack(blockToDestroy.getBlock().getType()));
                  }
                } else if (CustomModelData == Main.CMDSpikyPistonEast) {
                  meta.setCustomModelData(Main.CMDSpikyPistonExtendedEast);

                  Location blockToDestroy = adjacentBlock.getLocation().add(1, 0, 0);
                  if (blockToDestroy.getBlock() != null && blockToDestroy.getBlock() != BlockType.AIR){
                    blockToDestroy.getBlock().setType(Material.AIR);
                    blockToDestroy.getBlock().getLocation().getWorld().dropItemNaturally(blockToDestroy.getBlock().getLocation(), new ItemStack(blockToDestroy.getBlock().getType()));
                  }
                } else if (CustomModelData == Main.CMDSpikyPistonSouth) {
                  meta.setCustomModelData(Main.CMDSpikyPistonExtendedSouth);

                  Location blockToDestroy = adjacentBlock.getLocation().add(0, 0, 1);
                  if (blockToDestroy.getBlock() != null && blockToDestroy.getBlock() != BlockType.AIR){
                    blockToDestroy.getBlock().setType(Material.AIR);
                    blockToDestroy.getBlock().getLocation().getWorld().dropItemNaturally(blockToDestroy.getBlock().getLocation(), new ItemStack(blockToDestroy.getBlock().getType()));
                  }
                } else if (CustomModelData == Main.CMDSpikyPistonWest) {
                  meta.setCustomModelData(Main.CMDSpikyPistonExtendedWest);

                  Location blockToDestroy = adjacentBlock.getLocation().add(-1, 0, 0);
                  if (blockToDestroy.getBlock() != null && blockToDestroy.getBlock() != BlockType.AIR){
                    blockToDestroy.getBlock().setType(Material.AIR);
                    blockToDestroy.getBlock().getLocation().getWorld().dropItemNaturally(blockToDestroy.getBlock().getLocation(), new ItemStack(blockToDestroy.getBlock().getType()));
                  }
                } else if (CustomModelData == Main.CMDSpikyPistonUp) {
                  meta.setCustomModelData(Main.CMDSpikyPistonExtendedUp);

                  Location blockToDestroy = adjacentBlock.getLocation().add(0, 1, 0);
                  if (blockToDestroy.getBlock() != null && blockToDestroy.getBlock() != BlockType.AIR){
                    blockToDestroy.getBlock().setType(Material.AIR);
                    blockToDestroy.getBlock().getLocation().getWorld().dropItemNaturally(blockToDestroy.getBlock().getLocation(), new ItemStack(blockToDestroy.getBlock().getType()));
                  }
                } else if (CustomModelData == Main.CMDSpikyPistonDown) {
                  meta.setCustomModelData(Main.CMDSpikyPistonExtendedDown);

                  Location blockToDestroy = adjacentBlock.getLocation().add(0, -1, 0);
                  if (blockToDestroy.getBlock() != null && blockToDestroy.getBlock() != BlockType.AIR){
                    blockToDestroy.getBlock().setType(Material.AIR);
                    blockToDestroy.getBlock().getLocation().getWorld().dropItemNaturally(blockToDestroy.getBlock().getLocation(), new ItemStack(blockToDestroy.getBlock().getType()));
                  }
                }

                meta.setDisplayName("§rSpiky Piston Extended");
                Barnacles.setItemMeta(meta);

                as.setInvisible(true);
                as.setGravity(false);
                as.setBodyYaw(0);
                as.setRotation(0, 0);
                as.setCanMove(false);
                as.setCustomNameVisible(false);
                as.setPersistent(true);

                as.getEquipment().setHelmet(Barnacles);

              }
            }
          }
        }
      }

      if (event.getNewCurrent() == 0) {
        Location blockLoc = adjacentBlock.getLocation();
        for (ArmorStand as : blockLoc.getWorld().getEntitiesByClass(ArmorStand.class)) {
          if (as.getLocation().getBlockX() == blockLoc.getBlockX() &&
            as.getLocation().getBlockY() == blockLoc.getBlockY() &&
            as.getLocation().getBlockZ() == blockLoc.getBlockZ()) {

            if (as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedNorth
              || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedEast
              || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedSouth
              || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedWest
              || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedUp
              || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedDown
              ){
              ItemMeta oldMeta = as.getEquipment().getHelmet().getItemMeta();

              as.getEquipment().getHelmet().setAmount(0);

              ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
              ItemMeta meta = Barnacles.getItemMeta();

              int CustomModelData = oldMeta.getCustomModelData();
              if (CustomModelData == Main.CMDSpikyPistonExtendedNorth){
                meta.setCustomModelData(Main.CMDSpikyPistonNorth);
              } else if (CustomModelData == Main.CMDSpikyPistonExtendedEast) {
                meta.setCustomModelData(Main.CMDSpikyPistonEast);
              } else if (CustomModelData == Main.CMDSpikyPistonExtendedSouth) {
                meta.setCustomModelData(Main.CMDSpikyPistonSouth);
              } else if (CustomModelData == Main.CMDSpikyPistonExtendedWest) {
                meta.setCustomModelData(Main.CMDSpikyPistonWest);
              } else if (CustomModelData == Main.CMDSpikyPistonExtendedUp) {
                meta.setCustomModelData(Main.CMDSpikyPistonUp);
              } else if (CustomModelData == Main.CMDSpikyPistonExtendedDown) {
                meta.setCustomModelData(Main.CMDSpikyPistonDown);
              }

              meta.setDisplayName("§rSpiky Piston");

              Barnacles.setItemMeta(meta);

              as.setInvisible(true);
              as.setGravity(false);
              as.setBodyYaw(0);
              as.setRotation(0, 0);
              as.setCanMove(false);
              as.setCustomNameVisible(false);
              as.setPersistent(true);

              as.getEquipment().setHelmet(Barnacles);
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (event.getBlock().getType().equals(Material.QUARTZ_BLOCK)) {
      Location blockLoc = event.getBlock().getLocation();
      for (ArmorStand as : blockLoc.getWorld().getEntitiesByClass(ArmorStand.class)) {
        if (as.getLocation().getBlockX() == blockLoc.getBlockX() &&
          as.getLocation().getBlockY() == blockLoc.getBlockY() &&
          as.getLocation().getBlockZ() == blockLoc.getBlockZ()) {
          if (as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedNorth
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedEast
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedSouth
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedWest
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedUp
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonExtendedDown
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonNorth
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonEast
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonSouth
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonWest
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonUp
            || as.getEquipment().getHelmet().hasItemMeta() && as.getEquipment().getHelmet().getItemMeta().hasCustomModelData() && as.getEquipment().getHelmet().getItemMeta().getCustomModelData() == Main.CMDSpikyPistonDown
          ){

            as.remove();

            ItemStack spikyPiston = new ItemStack(Material.NAUTILUS_SHELL);
            ItemMeta SpikyPistonMeta = spikyPiston.getItemMeta();
            SpikyPistonMeta.setDisplayName("§rSpiky Piston");
            SpikyPistonMeta.setCustomModelData(CMDSpikyPistonItem);
            spikyPiston.setItemMeta(SpikyPistonMeta);

            as.getWorld().dropItemNaturally(as.getLocation(), spikyPiston);
          }
        }
      }
    }
  }
}
