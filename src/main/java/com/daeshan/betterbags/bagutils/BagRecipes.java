package com.daeshan.betterbags.bagutils;

import com.daeshan.betterbags.BetterBagsCore;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class BagRecipes {
    private final BetterBagsCore plugin;

    public BagRecipes(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    public void setupRecipes() {
        plugin.bagManager.createBagItems();
        setSmallRecipe();
        setMediumRecipe();
        setLargeRecipe();
        setEnderRecipe();
    }

    private void setEnderRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "ender_bag");
        ItemStack ender = plugin.bagItems.get(plugin.bagManager.eb_name.content());
        ShapedRecipe eb = new ShapedRecipe(key, ender);
        eb.shape("$!$", "#%#", "$!$");
        eb.setIngredient('#', Material.LEATHER);
        eb.setIngredient('%', Material.CHEST);
        eb.setIngredient('$', Material.ENDER_PEARL);
        eb.setIngredient('!', Material.DIAMOND);
        plugin.getServer().addRecipe(eb);
    }

    private void setLargeRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "large_bag");
        ItemStack large = plugin.bagItems.get(plugin.bagManager.lb_name.content());
        ShapedRecipe lb = new ShapedRecipe(key, large);
        lb.shape("!#!", "#%#", "!#!");
        lb.setIngredient('#', Material.LEATHER);
        lb.setIngredient('%', Material.CHEST);
        lb.setIngredient('!', Material.DIAMOND);
        plugin.getServer().addRecipe(lb);
    }

    private void setMediumRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "medium_bag");
        ItemStack medium = plugin.bagItems.get(plugin.bagManager.mb_name.content());
        ShapedRecipe mb = new ShapedRecipe(key, medium);
        mb.shape("!#!", "#%#", "!#!");
        mb.setIngredient('#', Material.LEATHER);
        mb.setIngredient('%', Material.CHEST);
        mb.setIngredient('!', Material.GOLD_INGOT);
        plugin.getServer().addRecipe(mb);
    }

    private void setSmallRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "small_bag");
        ItemStack small = plugin.bagItems.get(plugin.bagManager.sb_name.content());
        ShapedRecipe sb = new ShapedRecipe(key, small);

        sb.shape("#$#", "#%#", "###");
        sb.setIngredient('#', Material.LEATHER);
        sb.setIngredient('%', Material.CHEST);
        sb.setIngredient('$', Material.STRING);

        plugin.getServer().addRecipe(sb);
    }

}
