package com.tscforum.betterbags.commands;

import com.tscforum.betterbags.Core;
import com.tscforum.betterbags.commands.manager.SubCommand;
import com.tscforum.betterbags.listeners.BagsEventsClass;
import com.tscforum.betterbags.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveBags extends SubCommand {

    private Core plugin;

     public GiveBags(Core plugin) {
        this.plugin = plugin;
    }

    public void onCommand(Player pp, String[] args) {

        if ((pp.hasPermission("bags.give")) || pp.isOp()) {
            if (args.length == 0) {
                pp.sendMessage(MessageManager.error + "You must specify a bag!");
                return;
            }

            switch (args[0]) {

                case "small": {
                    ItemStack i = plugin.bagItems.get(ChatColor.stripColor(BagsEventsClass.sb_name));
                    pp.getInventory().addItem(i);
                    break;
                }
                case "medium": {
                    ItemStack i = plugin.bagItems.get(ChatColor.stripColor(BagsEventsClass.mb_name));
                    pp.getInventory().addItem(i);
                    break;
                }
                case "large": {
                    ItemStack i = plugin.bagItems.get(ChatColor.stripColor(BagsEventsClass.lb_name));
                    pp.getInventory().addItem(i);
                    break;
                }
                case "ender": {
                    ItemStack i = plugin.bagItems.get(ChatColor.stripColor(BagsEventsClass.eb_name));
                    pp.getInventory().addItem(i);
                    break;
                }
                default:
                    pp.sendMessage(MessageManager.error + "You do not have permission for this command");
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
