package com.tscforum.betterbags.recipes;

import com.tscforum.betterbags.Core;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class LargeBagRecipe {

    private Core plugin;

    public LargeBagRecipe(Core plugin) {
        this.plugin = plugin;
    }

    private NamespacedKey key = new NamespacedKey(plugin, "large_bag");
    private ItemStack large = (ItemStack) plugin.bagItems.get("Large Bag");
    private ShapedRecipe lb = new ShapedRecipe(key, large);

    public void setLargeRecipe() {
        lb.shape("!#!", "#%#", "!#!");
        lb.setIngredient('#', Material.LEATHER);
        lb.setIngredient('%', Material.CHEST);
        lb.setIngredient('!', Material.DIAMOND);
        plugin.getServer().addRecipe(lb);
    }
}
