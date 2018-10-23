package com.tscforum.betterbags.recipes;

import com.tscforum.betterbags.Core;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class SmallBagRecipe {

    private Core plugin;

    public SmallBagRecipe(Core plugin){
        this.plugin = plugin;
    }

    private NamespacedKey key = new NamespacedKey(plugin, "small_bag");
    private ItemStack small = (ItemStack) plugin.bagItems.get("Small Bag");
    private ShapedRecipe sb = new ShapedRecipe(key, small);

    public void setSmallRecipe(){
        sb.shape("#$#", "#%#", "###");
        sb.setIngredient('#', Material.LEATHER);
        sb.setIngredient('%', Material.CHEST);
        sb.setIngredient('$', Material.STRING);

        plugin.getServer().addRecipe(sb);
    }

}
