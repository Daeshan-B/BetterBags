package com.daeshan.betterbags.bagutils;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.utils.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class BagManager {

    private final BetterBagsCore plugin;
    public TextComponent eb_name = Component.text(ChatColor.DARK_PURPLE + "Ender Bag");
    public TextComponent sb_name = Component.text(ChatColor.DARK_GREEN + "Small Bag");
    public TextComponent mb_name = Component.text(ChatColor.GOLD + "Medium Bag");
    public TextComponent lb_name = Component.text(ChatColor.RED + "Large Bag");

    public BagManager(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    public Inventory getBag(TextComponent bagNameComponent) {
        if (bagNameComponent.equals(sb_name)) {
            return Bukkit.getServer().createInventory(null, 18, sb_name);
        } else if (bagNameComponent.equals(mb_name)) {
            return Bukkit.getServer().createInventory(null, 27, mb_name);
        } else if (bagNameComponent.equals(lb_name)) {
            return Bukkit.getServer().createInventory(null, 54, lb_name);
        } else if (bagNameComponent.equals(eb_name)) {
            return Bukkit.getServer().createInventory(null, 27, eb_name);
        }

        return null;
    }

    public void createBagItems() {
        ArrayList<TextComponent> bags = new ArrayList<>();
        bags.add(sb_name);
        bags.add(mb_name);
        bags.add(lb_name);
        bags.add(eb_name);

        for (TextComponent name : bags) {
            ItemStack bag = new ItemStack(Material.CHEST, 1);
            ItemMeta meta = bag.getItemMeta();
            ArrayList<Component> componentArrayList = new ArrayList<>();
            if (name == eb_name) {
                bag.setType(Material.ENDER_CHEST);
                componentArrayList.add(Component.text("Links With your Ender Chest."));
            }
            componentArrayList.add(Component.text(ChatColor.WHITE + "Size: " + ChatColor.GRAY + getBag(name).getSize()));
            meta.lore(componentArrayList);
            meta.displayName(name);
            bag.setItemMeta(meta);
            plugin.bagItems.put(name.content(), bag);
            TextComponent textComponent = (TextComponent) bag.getItemMeta().displayName();
            MessageManager.debug("Bag: " +  textComponent.content() + " added to HashMap");
        }

        MessageManager.debug("Bags added to HashMap");

    }

}
