package com.daeshan.betterbags.bagutils;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;


public class BagManager {

    private BetterBagsCore plugin;

    public BagManager(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    public String eb_name = ChatColor.DARK_PURPLE + "Ender Bag";
    public String sb_name = ChatColor.DARK_GREEN + "Small Bag";
    public String mb_name = ChatColor.GOLD + "Medium Bag";
    public String lb_name = ChatColor.RED + "Large Bag";

    public Inventory getBag(String bagname) {
        if (bagname.equals(sb_name)) {
            return Bukkit.getServer().createInventory(null, 18, sb_name);
        } else if (bagname.equals(mb_name)) {
            return Bukkit.getServer().createInventory(null, 27, mb_name);
        } else if (bagname.equals(lb_name)) {
            return Bukkit.getServer().createInventory(null, 54, lb_name);
        } else if (bagname.equals(eb_name)) {
            return Bukkit.getServer().createInventory(null, 27, eb_name);
        }

        return null;
    }

    public void createBagItems() {
        ArrayList<String> bags = new ArrayList<>();
        bags.add(sb_name);
        bags.add(mb_name);
        bags.add(lb_name);
        bags.add(eb_name);


        for (String name : bags) {
            ItemStack bag = new ItemStack(Material.CHEST, 1);
            ItemMeta meta = bag.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            if (name.equals(eb_name)) {
                bag.setType(Material.ENDER_CHEST);
                lore.add("Links With your Ender Chest.");
            }
            lore.add(ChatColor.WHITE + "Size: " + ChatColor.GRAY + getBag(name).getSize());
            meta.setLore(lore);
            meta.setDisplayName(name);
            bag.setItemMeta(meta);
            plugin.bagItems.put(name, bag);
            MessageManager.debug("Bag: " + bag.getItemMeta().getDisplayName() + " added to HashMap");
        }

        MessageManager.debug("Bags added to HashMap");

    }

}
