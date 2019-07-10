package com.daeshan.betterbags.listeners;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BagCloseEvent implements Listener {
    private BetterBagsCore plugin;

    public BagCloseEvent(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void closeBag(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            String uuid = player.getUniqueId().toString();
            Inventory closed = event.getInventory();
            String closedname = ChatColor.stripColor(closed.getTitle());
            HashMap<String, List> inventories = (HashMap<String, List>) plugin.nitRiteData.getFromDocument("uuid", uuid, "inventories");
            for (String bagnames : inventories.keySet()) {
                if (closedname.equalsIgnoreCase(bagnames)) {
                    List<Map> itemserialized = new ArrayList<>();
                    if (bagnames.equalsIgnoreCase("ender")) {
                        player.getEnderChest().clear();
                        for (ItemStack itemStack : closed.getContents()) {
                            if (itemStack != null) {
                                player.getEnderChest().addItem(itemStack);
                                itemserialized.add(itemStack.serialize());
                            }
                        }
                    } else {
                        for (ItemStack itemStack : closed.getContents()) {
                            if (itemStack != null) {
                                itemserialized.add(itemStack.serialize());
                            }
                        }
                    }
                    inventories.put(closedname, itemserialized);
                    plugin.nitRiteData.setInDocument("uuid", uuid, "inventories", inventories);
                    MessageManager.debug("Bag saved to file.");
                }
            }
        }
    }
}
