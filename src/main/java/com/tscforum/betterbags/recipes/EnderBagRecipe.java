package com.tscforum.betterbags.recipes;

import com.tscforum.betterbags.Core;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class EnderBagRecipe {

    private Core plugin;

    public EnderBagRecipe(Core plugin) {
        this.plugin = plugin;
    }

    private NamespacedKey key = new NamespacedKey(plugin, "ender_bag");
    private ItemStack ender = (ItemStack) plugin.bagItems.get("Ender Bag");
    private ShapedRecipe eb = new ShapedRecipe(key, ender);

    public void setEnderRecipe() {
        eb.shape("$!$", "#%#", "$!$");
        eb.setIngredient('#', Material.LEATHER);
        eb.setIngredient('%', Material.CHEST);
        eb.setIngredient('$', Material.ENDER_PEARL);
        eb.setIngredient('!', Material.DIAMOND);
        plugin.getServer().addRecipe(eb);
    }
}
