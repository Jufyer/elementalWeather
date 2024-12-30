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
import org.jufyer.plugin.aquatic.commands.SpawnShark;
import org.jufyer.plugin.aquatic.nibblers.listeners.NibblerListeners;
import org.jufyer.plugin.aquatic.prismarineOceanRuin.GeneratePrismarineOceanRuin;
import org.jufyer.plugin.aquatic.commands.SpawnOceanGlider;
import org.jufyer.plugin.aquatic.oceanGlider.entity.listeners.OceanGliderListeners;
import org.jufyer.plugin.aquatic.shark.listeners.SharkListeners;
import org.jufyer.plugin.aquatic.spikyPiston.listeners.SpikyPistonListeners;
import org.jufyer.plugin.aquatic.whale.entity.Whale;
import org.jufyer.plugin.aquatic.whale.listeners.*;

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
  public static final int CMDSharkTooth = 1982015;

  public static final int CMDSpikyPistonItem = 191619;

  public static final int CMDSpikyPistonNorth = 191641;
  public static final int CMDSpikyPistonEast = 19165;
  public static final int CMDSpikyPistonSouth = 191619;
  public static final int CMDSpikyPistonWest = 191623;
  public static final int CMDSpikyPistonUp = 191621;
  public static final int CMDSpikyPistonDown = 19164;

  public static final int CMDSpikyPistonExtendedNorth = 1916514;
  public static final int CMDSpikyPistonExtendedEast = 191655;
  public static final int CMDSpikyPistonExtendedSouth = 1916519;
  public static final int CMDSpikyPistonExtendedWest = 1916523;
  public static final int CMDSpikyPistonExtendedUp = 1916521;
  public static final int CMDSpikyPistonExtendedDown = 191654;

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
    getCommand("spawnShark").setExecutor(new SpawnShark());

    Bukkit.getPluginManager().registerEvents(new spawnListener(), this);
    Bukkit.getPluginManager().registerEvents(new rightClickListener(), this);
    Bukkit.getPluginManager().registerEvents(new generateStructure(), this);
    Bukkit.getPluginManager().registerEvents(new customBlockListeners(), this);
    Bukkit.getPluginManager().registerEvents(new OceanGliderListeners(), this);
    Bukkit.getPluginManager().registerEvents(new GeneratePrismarineOceanRuin(), this);
    Bukkit.getPluginManager().registerEvents(new NibblerListeners(), this);
    Bukkit.getPluginManager().registerEvents(new SharkListeners(), this);
    Bukkit.getPluginManager().registerEvents(new WhaleListeners(), this);
    Bukkit.getPluginManager().registerEvents(new SpikyPistonListeners(), this);

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

    ItemStack sharkTooth = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta SharkToothMeta = sharkTooth.getItemMeta();
    SharkToothMeta.setDisplayName("§rShark Tooth");
    SharkToothMeta.setCustomModelData(Main.CMDSharkTooth);
    sharkTooth.setItemMeta(SharkToothMeta);

    ItemStack spikyPiston = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta SpikyPistonMeta = spikyPiston.getItemMeta();
    SpikyPistonMeta.setDisplayName("§rSpiky Piston");
    SpikyPistonMeta.setCustomModelData(CMDSpikyPistonItem);
    spikyPiston.setItemMeta(SpikyPistonMeta);

    ShapedRecipe SpikyPiston = new ShapedRecipe(spikyPiston);
    SpikyPiston.shape("TTT", "TPT", "   ");

    SpikyPiston.setIngredient('T', new RecipeChoice.ExactChoice(sharkTooth)); // Custom Item
    SpikyPiston.setIngredient('P', Material.PISTON);

    getServer().addRecipe(SpikyPiston);
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
