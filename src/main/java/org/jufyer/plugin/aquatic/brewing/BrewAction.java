package org.jufyer.plugin.aquatic.brewing;

import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;

public class BrewAction {
    public void brew(BrewingStand stand, BrewingRecipe recipe, int slot){
        BrewerInventory inv = stand.getInventory();
        inv.setItem(slot, recipe.getResult());
    }
}
