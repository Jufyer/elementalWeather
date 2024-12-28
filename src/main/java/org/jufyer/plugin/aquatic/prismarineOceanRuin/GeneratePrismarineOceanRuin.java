package org.jufyer.plugin.aquatic.prismarineOceanRuin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.AsyncStructureSpawnEvent;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jufyer.plugin.aquatic.Main;

import java.util.Random;

public class GeneratePrismarineOceanRuin implements Listener {

  @EventHandler
  public void onAsyncStructureSpawn(AsyncStructureSpawnEvent event) {
    if (event.getStructure().getStructureType().equals(StructureType.OCEAN_RUIN)) {
      if (new Random().nextDouble() < 0.5) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
          Location loc = new Location(event.getWorld(), event.getBoundingBox().getCenterX(), event.getBoundingBox().getMinY(), event.getBoundingBox().getCenterZ());
          if (loc.getWorld().getBiome(loc) == Biome.COLD_OCEAN || loc.getWorld().getBiome(loc) == Biome.OCEAN || loc.getWorld().getBiome(loc) == Biome.DEEP_COLD_OCEAN || loc.getWorld().getBiome(loc) == Biome.DEEP_OCEAN || loc.getWorld().getBiome(loc) == Biome.DEEP_COLD_OCEAN || loc.getWorld().getBiome(loc) == Biome.WARM_OCEAN){
            placeBlocks(loc);
          }
        });
        event.setCancelled(true);
      }
    }
  }

  private void placeBlocks(Location location) {
    Location baseGroundLocation = getGroundLocation(location);

    Object[][] offsets = {
      {0, -1, 2, Material.PRISMARINE},
      {1, -1, 4, Material.PRISMARINE},
      {0, -1, -1, Material.PRISMARINE},
      {0, -1, 3, Material.PRISMARINE},
      {1, -1, 3, Material.PRISMARINE},
      {0, -1, 0, Material.PRISMARINE},
      {1, -1, 2, Material.PRISMARINE},
      {0, -1, 1, Material.PRISMARINE},
      {1, -1, 1, Material.PRISMARINE},
      {-2, -1, -2, Material.PRISMARINE},
      {1, -1, 0, Material.PRISMARINE},
      {-2, -1, -1, Material.PRISMARINE},
      {0, 1, -1, Material.DARK_PRISMARINE},
      {0, 1, 1, Material.PRISMARINE_SLAB, BlockFace.DOWN},
      {2, -1, 1, Material.PRISMARINE},
      {1, 0, 3, Material.DARK_PRISMARINE},
      {2, -1, 0, Material.PRISMARINE_SLAB, BlockFace.DOWN},
      {1, 0, 2, Material.PRISMARINE},
      {2, -1, 3, Material.PRISMARINE},
      {2, -1, 2, Material.PRISMARINE},
      {0, -1, 4, Material.PRISMARINE},
      {1, 0, 0, Material.DARK_PRISMARINE},
      {1, 3, 1, Material.PRISMARINE_SLAB, BlockFace.DOWN},
      {-1, 1, 2, Material.PRISMARINE},
      {1, 3, 3, Material.DARK_PRISMARINE_STAIRS, BlockFace.WEST},
      {-1, 2, -1, Material.DARK_PRISMARINE},
      {-1, 1, 4, Material.DARK_PRISMARINE},
      {-1, 1, 0, Material.DARK_PRISMARINE_STAIRS, BlockFace.WEST},
      {-2, 0, 2, Material.PRISMARINE},
      {-2, 0, 1, Material.PRISMARINE_BRICKS},
      {-3, -1, 1, Material.PRISMARINE},
      {-2, 0, 0, Material.DARK_PRISMARINE},
      {-3, -1, 0, Material.PRISMARINE_BRICKS},
      {-2, -1, 0, Material.PRISMARINE},
      {-2, -1, 1, Material.PRISMARINE},
      {-2, -1, 2, Material.PRISMARINE},
      {-1, 1, -1, Material.DARK_PRISMARINE},
      {0, 0, 1, Material.PRISMARINE},
      {0, 0, 3, Material.PRISMARINE},
      {0, 0, 2, Material.PRISMARINE},
      {0, 0, 4, Material.DARK_PRISMARINE},
      {-1, 0, 0, Material.CHEST, BlockFace.NORTH},
      {1, 2, 1, Material.DARK_PRISMARINE},
      {-1, 0, 1, Material.PRISMARINE},
      {1, 2, 2, Material.DARK_PRISMARINE},
      {-1, 0, 2, Material.PRISMARINE},
      {1, 2, 3, Material.DARK_PRISMARINE},
      {-2, 1, 3, Material.DARK_PRISMARINE},
      {-1, 0, 4, Material.DARK_PRISMARINE},
      {-2, 1, 2, Material.PRISMARINE_BRICKS},
      {-2, 1, 1, Material.PRISMARINE_STAIRS, BlockFace.EAST},
      {-2, 1, 0, Material.DARK_PRISMARINE},
      {-3, -1, -1, Material.PRISMARINE},
      {1, -1, -1, Material.PRISMARINE_BRICKS},
      {0, 3, 3, Material.DARK_PRISMARINE},
      {1, 1, 0, Material.DARK_PRISMARINE},
      {-1, 3, 1, Material.DARK_PRISMARINE},
      {0, 3, 2, Material.DARK_PRISMARINE},
      {-1, 3, 2, Material.DARK_PRISMARINE},
      {0, 3, 1, Material.PRISMARINE_BRICKS},
      {0, 3, 0, Material.DARK_PRISMARINE},
      {-1, 3, 0, Material.DARK_PRISMARINE},
      {-2, 2, 1, Material.DARK_PRISMARINE},
      {-2, 2, 2, Material.PRISMARINE_BRICKS},
      {-2, 2, 3, Material.DARK_PRISMARINE},
      {-1, -1, -1, Material.PRISMARINE},
      {-1, -1, 3, Material.PRISMARINE},
      {-1, -1, 2, Material.PRISMARINE},
      {-1, -1, 1, Material.PRISMARINE},
      {-2, 2, 0, Material.PRISMARINE_SLAB, BlockFace.DOWN},
      {-1, -1, 0, Material.PRISMARINE_BRICKS},
      {-2, 0, -1, Material.PRISMARINE_SLAB, BlockFace.DOWN}
    };

    for (Object[] offset : offsets) {
      int x = (int) offset[0];
      int y = (int) offset[1];
      int z = (int) offset[2];
      Material material = (Material) offset[3];
      BlockFace rotation = (offset.length > 4) ? (BlockFace) offset[4] : BlockFace.SELF;

      Location blockLocation = baseGroundLocation.clone().add(x, y, z);

      Block block = blockLocation.getBlock();
      block.setType(material);

      if (material.createBlockData() instanceof Directional directional) {
        directional.setFacing(rotation);
        block.setBlockData(directional);
      } else if (material.createBlockData() instanceof Slab slab) {
        slab.setType(rotation == BlockFace.DOWN ? Slab.Type.BOTTOM : Slab.Type.TOP);
        block.setBlockData(slab);
      } else if (material.createBlockData() instanceof Stairs stairs) {
        stairs.setFacing(rotation);
        block.setBlockData(stairs);
      }

      if (material == Material.CHEST) {
        Inventory chestInventory = ((Chest) block.getState()).getInventory();
        generateLootTable(chestInventory);
      }
    }
  }

  private Location getGroundLocation(Location location) {
    Location groundLocation = location.clone();

    while (groundLocation.getY() > 0) {
      Block block = groundLocation.getBlock();
      if (!block.isEmpty() && !block.isLiquid()) {
        groundLocation.add(0, 1, 0);
        break;
      }
      groundLocation.add(0, -1, 0);
    }

    return groundLocation;
  }

  private void generateLootTable(Inventory inventory) {
    ItemStack[] loot = {
      new ItemStack(Material.DIAMOND, 1),
      new ItemStack(Material.IRON_INGOT, 1),
      new ItemStack(Material.GOLD_INGOT, 1),
      new ItemStack(Material.APPLE, 1)
    };

    Random random = new Random();
    for (int i = 0; i < 10; i++) {
      ItemStack randomItem = loot[random.nextInt(loot.length)];
      int randomNum = random.nextInt(inventory.getSize());
      while (inventory.getItem(randomNum) != null && inventory.getItem(randomNum).getType() != Material.AIR || inventory.getItem(randomNum) != null && inventory.getItem(randomNum).getType() == randomItem.getType()) {
        randomNum = random.nextInt(inventory.getSize());
      }
      inventory.setItem(randomNum, randomItem);
    }
  }
}
