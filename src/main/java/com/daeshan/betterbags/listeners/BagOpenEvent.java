package com.daeshan.betterbags.listeners;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BagOpenEvent implements Listener {

    private BetterBagsCore plugin;

    public BagOpenEvent(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void clickItems(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        ItemStack clicked = event.getItem();

        if (clicked == null || !clicked.hasItemMeta()) return;

        ItemMeta itemMeta = clicked.getItemMeta();
        List<String> lorelist = itemMeta.getLore();

        if (!lorelist.contains(ChatColor.GRAY + uuid)) {
            MessageManager.errorPlayer(player, "This bag does not belong to you. You can not open it.");
            return;
        }

        for (String string : lorelist) {
            String lore = ChatColor.stripColor(string);
            HashMap<String, List> inventories = (HashMap<String, List>) plugin.nitRiteData.getFromDocument("uuid", uuid, "inventories");
            for (String bagidentifier : inventories.keySet()) {
                if (lore.equalsIgnoreCase(bagidentifier)) {
                    Inventory tmpinven = plugin.bagManager.getBag(itemMeta.getDisplayName());
                    Inventory inventory = Bukkit.createInventory(null, tmpinven.getSize(), bagidentifier);

                    List<Map> itemmap = (List<Map>) inventories.get(bagidentifier);
                    if (!itemmap.isEmpty()) {
                        for (Map serialized : itemmap) {
                            inventory.addItem(ItemStack.deserialize(serialized));
                        }
                    }
                    player.openInventory(inventory);
                } else if (bagidentifier.equalsIgnoreCase("ENDER")) {
                    Inventory inventory = Bukkit.createInventory(null, player.getEnderChest().getSize(), bagidentifier);

                    for (ItemStack items : player.getEnderChest().getContents()) {
                        if (items == null) continue;
                        inventory.addItem(items);
                    }
                    player.openInventory(inventory);
                }
            }
        }
    }
}
