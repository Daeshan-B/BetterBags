package com.tscforum.betterbags.recipes;

import com.tscforum.betterbags.Core;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class MediumBagRecipe {

    private Core plugin;

    public MediumBagRecipe(Core plugin){
        this.plugin = plugin;
    }

    private NamespacedKey key = new NamespacedKey(plugin, "medium_bag");
    private ItemStack medium = (ItemStack) plugin.bagItems.get("Medium Bag");
    private ShapedRecipe mb = new ShapedRecipe(key, medium);

    public void setMediumRecipe(){
        mb.shape("!#!", "#%#", "!#!");
        mb.setIngredient('#', Material.LEATHER);
        mb.setIngredient('%', Material.CHEST);
        mb.setIngredient('!', Material.GOLD_INGOT);
        plugin.getServer().addRecipe(mb);
    }
}
