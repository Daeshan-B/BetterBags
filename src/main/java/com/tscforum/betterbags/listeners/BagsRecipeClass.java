package com.tscforum.betterbags.listeners;

import com.tscforum.betterbags.BagMainClass;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class BagsRecipeClass
        implements Listener
{
    private BagMainClass plugin = (BagMainClass)BagMainClass.getPlugin(BagMainClass.class);

    public void setRecipe()
    {
        ItemStack small = (ItemStack)this.plugin.bagItems.get("Small Bag");
        ItemStack medium = (ItemStack)this.plugin.bagItems.get("Medium Bag");
        ItemStack large = (ItemStack)this.plugin.bagItems.get("Large Bag");
        ItemStack ender = (ItemStack)this.plugin.bagItems.get("Ender Bag");

        ShapedRecipe sb = new ShapedRecipe(small);
        ShapedRecipe mb = new ShapedRecipe(medium);
        ShapedRecipe lb = new ShapedRecipe(large);
        ShapedRecipe eb = new ShapedRecipe(ender);

        sb.shape("#$#", "#%#", "###");
        sb.setIngredient('#', Material.LEATHER);
        sb.setIngredient('%', Material.CHEST);
        sb.setIngredient('$', Material.STRING);

        this.plugin.getServer().addRecipe(sb);

        mb.shape("!#!", "#%#", "!#!");
        mb.setIngredient('#', Material.LEATHER);
        mb.setIngredient('%', Material.CHEST);
        mb.setIngredient('!', Material.GOLD_INGOT);
        this.plugin.getServer().addRecipe(mb);

        lb.shape("!#!", "#%#", "!#!");
        lb.setIngredient('#', Material.LEATHER);
        lb.setIngredient('%', Material.CHEST);
        lb.setIngredient('!', Material.DIAMOND);
        this.plugin.getServer().addRecipe(lb);

        eb.shape("$!$", "#%#", "$!$");
        eb.setIngredient('#', Material.LEATHER);
        eb.setIngredient('%', Material.CHEST);
        eb.setIngredient('$', Material.ENDER_PEARL);
        eb.setIngredient('!', Material.DIAMOND);
        this.plugin.getServer().addRecipe(eb);
    }
}
