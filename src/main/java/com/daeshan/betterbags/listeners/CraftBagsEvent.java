package com.daeshan.betterbags.listeners;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CraftBagsEvent implements Listener {
    private BetterBagsCore plugin;

    public CraftBagsEvent(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void craftBagEvent(CraftItemEvent event) {
        ItemStack craftedItem = event.getCurrentItem();
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String uuid = player.getUniqueId().toString();

            if (plugin.bagItems.containsKey(craftedItem.getItemMeta().getDisplayName())) {
                String name = craftedItem.getItemMeta().getDisplayName();
                int bagcount = (int) plugin.nitRiteData.getFromDocument("uuid", uuid, "bag.count");
                bagcount += 1;
                plugin.nitRiteData.setInDocument("uuid", uuid, "bag.count", bagcount);

                ItemMeta itemMeta = craftedItem.getItemMeta();
                List<String> lore = itemMeta.getLore();
                if (craftedItem.getType().equals(Material.ENDER_CHEST)) {
                    lore.add(ChatColor.GRAY + "ENDER");
                } else {
                    lore.add(ChatColor.WHITE + "BAG#0" + bagcount);
                }
                lore.add(ChatColor.GRAY + uuid);
                itemMeta.setLore(lore);
                craftedItem.setItemMeta(itemMeta);

                MessageManager.debug("Player crafted a bag");
                HashMap<String, List> inventories;

                if (bagcount == 1) {
                    inventories = new HashMap<>();
                    putInventory(inventories, bagcount, uuid, name);
                } else {
                    inventories = (HashMap<String, List>) plugin.nitRiteData.getFromDocument("uuid", uuid, "inventories");
                    putInventory(inventories, bagcount, uuid, name);
                }
            }
        }

    }

    private void putInventory(HashMap<String, List> inventories, int bagcount, String uuid, String bagname) {
        Inventory inventory = plugin.bagManager.getBag(bagname);
        List<Map> items = new ArrayList<>();
        if (!bagname.equalsIgnoreCase(plugin.bagManager.eb_name)) {
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack == null) continue;
                items.add(itemStack.serialize());
            }
            inventories.put("BAG#0" + bagcount, items);
        } else if (bagname.equalsIgnoreCase(plugin.bagManager.eb_name)) {
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            for (ItemStack itemStack : player.getEnderChest().getContents()) {
                if (itemStack == null) continue;
                items.add(itemStack.serialize());
            }
            inventories.put("ENDER", items);
        }
        plugin.nitRiteData.setInDocument("uuid", uuid, "inventories", inventories);
        MessageManager.debug("Added inventory data for player: " + uuid);
    }
}
