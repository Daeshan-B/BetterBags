package com.daeshan.betterbags.commands;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.commands.manager.SubCommand;
import com.daeshan.betterbags.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveBags extends SubCommand {

    private BetterBagsCore plugin;

    public GiveBags(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    public void onCommand(Player player, String[] args) {

        if ((player.hasPermission("bags.give")) || player.isOp()) {
            if (args.length == 0) {
                MessageManager.errorPlayer(player, "You must specify a bag!");
                return;
            }

            switch (args[0]) {

                case "small": {
                    ItemStack i = plugin.bagItems.get(ChatColor.stripColor(plugin.bagManager.sb_name));
                    player.getInventory().addItem(i);
                    break;
                }
                case "medium": {
                    ItemStack i = plugin.bagItems.get(ChatColor.stripColor(plugin.bagManager.mb_name));
                    player.getInventory().addItem(i);
                    break;
                }
                case "large": {
                    ItemStack i = plugin.bagItems.get(ChatColor.stripColor(plugin.bagManager.lb_name));
                    player.getInventory().addItem(i);
                    break;
                }
                case "ender": {
                    ItemStack i = plugin.bagItems.get(ChatColor.stripColor(plugin.bagManager.eb_name));
                    player.getInventory().addItem(i);
                    break;
                }
                default:
                    MessageManager.errorPlayer(player, "You do not have permission for this command");
            }
        }
    }

    public String name() {
        return "give";
    }

    public String info() {
        return "give";
    }

    public String[] aliases() {
        return new String[]{"g"};
    }
}
