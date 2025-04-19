package org.jufyer.plugin.aquatic.brewing;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class BrewingControler {
    private List<BrewingRecipe> recipes;
    private Listener potionEventListner;

    public BrewingControler(){}

    public BrewingControler(Plugin plugin) {
        recipes = new ArrayList<BrewingRecipe>();
        this.start(plugin);
    }
    
    public void start(Plugin plugin){
        this.stop();
        potionEventListner = new PotionEvent(plugin, this);
        plugin.getServer().getPluginManager().registerEvents(potionEventListner, plugin);
    }

    public void stop(){
        if(potionEventListner != null){
            HandlerList.unregisterAll(potionEventListner);
            potionEventListner = null;
        }
    }

    public void addRecipe(BrewingRecipe recipe){
        this.recipes.add(recipe);
    }
    
    public void removeRecipe(BrewingRecipe recipe){
        this.recipes.remove(recipe);
    }

    public BrewingRecipe getRecipe(NamespacedKey key){
        BrewingRecipe ret = null;
        for(BrewingRecipe recipe : this.recipes){
            if(recipe.getKey().equals(key)){
                ret = recipe;
                break;
            }
        }
        return ret;
    }

    public BrewingRecipe getRecipe(ItemStack inputIngredient, ItemStack inputBase){
        BrewingRecipe ret = null;
        for(BrewingRecipe recipe : this.recipes){
            ItemStack rIng = recipe.getInputIngredient();
            ItemStack rBase = recipe.getInputBase();
            if(rIng.isSimilar(inputIngredient) && rIng.getAmount() <= inputIngredient.getAmount()
                && rBase.equals(inputBase)){
                ret = recipe;
                break;
            }
        }
        return ret;
    }

    public BrewingRecipe getRecipe(ItemStack inputIngredient, ItemStack inputBase, int fuel){
        BrewingRecipe ret = null;
        for(BrewingRecipe recipe : this.recipes){
            ItemStack rIng = recipe.getInputIngredient();
            ItemStack rBase = recipe.getInputBase();
            if(rIng.isSimilar(inputIngredient) && rIng.getAmount() <= inputIngredient.getAmount()
                && rBase.equals(inputBase) && fuel >= recipe.getFuelUse()){
                ret = recipe;
                break;
            }
        }
        return ret;
    }

    public static int totalFuelInBrewingStand(BrewingStand stand){
        int total = stand.getFuelLevel();
        if(stand.getInventory().getFuel() != null && stand.getInventory().getFuel().getType() == Material.BLAZE_POWDER){
            total += 20 * stand.getInventory().getFuel().getAmount();
        }
        return total;
    }

    public List<BrewingRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<BrewingRecipe> recipes) {
        this.recipes = recipes;
    }

    public void clearRecipes(){
        this.recipes = new ArrayList<BrewingRecipe>();
    }
}
