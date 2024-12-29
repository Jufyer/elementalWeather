package org.jufyer.plugin.aquatic;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Slab;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.aquatic.commands.GiveOceanGlider;
import org.jufyer.plugin.aquatic.commands.SpawnNibbler;
import org.jufyer.plugin.aquatic.nibblers.entity.Nibbler;
import org.jufyer.plugin.aquatic.nibblers.listeners.NibblerListeners;
import org.jufyer.plugin.aquatic.prismarineOceanRuin.GeneratePrismarineOceanRuin;
import org.jufyer.plugin.aquatic.commands.SpawnOceanGlider;
import org.jufyer.plugin.aquatic.oceanGlider.entity.listeners.OceanGliderListeners;
import org.jufyer.plugin.aquatic.whale.entity.Whale;
import org.jufyer.plugin.aquatic.whale.listeners.customBlockListeners;
import org.jufyer.plugin.aquatic.whale.listeners.generateStructure;
import org.jufyer.plugin.aquatic.whale.listeners.rightClickListener;
import org.jufyer.plugin.aquatic.whale.listeners.spawnListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin implements Listener {
  private static Main instance;

  public static final int CMDBarnacle = 21;
  public static final int CMDBarnacleSpike = 219;
  public static final int CMDBarnacleSpikeExtended = 2195;
  public static final int CMDOceanGlider = 157;
  public static final int CMDOceanGliderEntity = 1575;
  public static final int CMDNibblerBucket = 149221;

  public static Main getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;

    getCommand("spawnOceanGlider").setExecutor(new SpawnOceanGlider());
    getCommand("giveOceanGlider").setExecutor(new GiveOceanGlider());
    getCommand("spawnWhale").setExecutor(this);
    getCommand("spawnNibbler").setExecutor(new SpawnNibbler());

    Bukkit.getPluginManager().registerEvents(new spawnListener(), this);
    Bukkit.getPluginManager().registerEvents(new rightClickListener(), this);
    Bukkit.getPluginManager().registerEvents(new generateStructure(), this);
    Bukkit.getPluginManager().registerEvents(new customBlockListeners(), this);
    Bukkit.getPluginManager().registerEvents(new OceanGliderListeners(), this);
    Bukkit.getPluginManager().registerEvents(new GeneratePrismarineOceanRuin(), this);
    Bukkit.getPluginManager().registerEvents(new NibblerListeners(), this);

    //Custom Recipe:
    ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = Barnacles.getItemMeta();
    if (meta != null) {
      meta.setDisplayName("§rBarnacle Spike");
      meta.setCustomModelData(CMDBarnacleSpike);
      Barnacles.setItemMeta(meta);
    }

    ItemStack Barnacle = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta Bmeta = Barnacle.getItemMeta();
    Bmeta.setDisplayName("§rBarnacle");
    Bmeta.setCustomModelData(CMDBarnacle);
    Barnacle.setItemMeta(Bmeta);

    ShapedRecipe barnacles_spike = new ShapedRecipe(Barnacles);
    barnacles_spike.shape(" B ", "BIB", " R ");

    barnacles_spike.setIngredient('B', new RecipeChoice.ExactChoice(Barnacle)); // Custom Item
    barnacles_spike.setIngredient('I', Material.IRON_INGOT);
    barnacles_spike.setIngredient('R', Material.REDSTONE);

    getServer().addRecipe(barnacles_spike);
  }

  @Override
  public void onDisable(){

  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Player player = (Player) sender;
    boolean isOP = player.isOp();

    if (isOP){
      new Whale(player.getLocation());
    }
    //saveBlockData(player);

    return false;
  }

  private void saveBlockData(Player player) {
    Map<String, String> blockData = new HashMap<>();
    List<Material> targetMaterials = List.of(
      Material.PRISMARINE,
      Material.PRISMARINE_SLAB,
      Material.PRISMARINE_STAIRS,
      Material.DARK_PRISMARINE,
      Material.DARK_PRISMARINE_STAIRS,
      Material.PRISMARINE_BRICKS,
      Material.CHEST
    );

    int radius = 15;
    for (int x = -radius; x <= radius; x++) {
      for (int y = -radius; y <= radius; y++) {
        for (int z = -radius; z <= radius; z++) {
          Block block = player.getLocation().add(x, y, z).getBlock();
          if (targetMaterials.contains(block.getType())) {
            String relativeCoordinates = x + ", " + y + ", " + z;
            String blockType = block.getType().name();
            String rotation = "NONE";

            // Check block data for rotation
            if (block.getBlockData() instanceof Directional) {
              Directional directional = (Directional) block.getBlockData();
              rotation = directional.getFacing().name();
            } else if (block.getBlockData() instanceof Orientable) {
              Orientable orientable = (Orientable) block.getBlockData();
              rotation = orientable.getAxis().name();
            } else if (block.getBlockData() instanceof Bisected) {
              Bisected bisected = (Bisected) block.getBlockData();
              rotation = bisected.getHalf().name();
            } else if (block.getBlockData() instanceof Slab) {
              Slab slab = (Slab) block.getBlockData();
              rotation = slab.getType().name();
            }

            blockData.put(relativeCoordinates, "Type: " + blockType + ", Rotation: " + rotation);
          }
        }
      }
    }

    blockData.forEach((coordinates, data) -> {
      getLogger().info("Coordinates: " + coordinates + " | " + data);
    });
  }
}
