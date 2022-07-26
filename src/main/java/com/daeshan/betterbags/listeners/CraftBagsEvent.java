package com.daeshan.betterbags.listeners;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.utils.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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
import org.w3c.dom.Text;

import java.util.*;

public class CraftBagsEvent implements Listener {
    private final BetterBagsCore plugin;

    public CraftBagsEvent(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void craftBagEvent(CraftItemEvent event) {
        ItemStack craftedItem = event.getCurrentItem();
        if (event.getWhoClicked() instanceof Player player) {
            String uuid = player.getUniqueId().toString();

            if (plugin.bagItems.containsKey(craftedItem.displayName())) {
                TextComponent name = (TextComponent) craftedItem.getItemMeta().displayName();
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
                HashMap<String, List<Map<String, Object>>> inventories;

                if (bagcount == 1) {
                    inventories = new HashMap<>();
                    putInventory(inventories, bagcount, uuid, name);
                } else {
                    inventories = (HashMap<String, List<Map<String, Object>>>) plugin.nitRiteData.getFromDocument("uuid", uuid, "inventories");
                    putInventory(inventories, bagcount, uuid, name);
                }
            }
        }

    }

    private void putInventory(HashMap<String, List<Map<String, Object>>> inventories, int bagCount, String uuid, TextComponent bagName) {
        Inventory inventory = plugin.bagManager.getBag(bagName);
        List<Map<String, Object>> items = new ArrayList<>();
        if (bagName != plugin.bagManager.eb_name) {
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack == null) continue;
                items.add(itemStack.serialize());
            }
            inventories.put("BAG#0" + bagCount, items);
        } else {
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
