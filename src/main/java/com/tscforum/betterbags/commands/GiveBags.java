package com.tscforum.betterbags.commands;

import com.tscforum.betterbags.BagMainClass;
import com.tscforum.betterbags.commands.manager.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveBags
        extends SubCommand
{
    private BagMainClass plugin = BagMainClass.getPlugin(BagMainClass.class);

    public void onCommand(Player pp, String[] args)
    {
        if ((pp.hasPermission("bags.give")) || (pp.isOp()))
        {
            if (args.length == 0)
            {
                pp.sendMessage(this.plugin.error + "You must specify a bag!");
                return;
            }
            if (args[0].equals("small"))
            {
                ItemStack i = this.plugin.bagItems.get(ChatColor.stripColor(this.plugin.bagsEvents.sb_name));
                pp.getInventory().addItem(i);
            }
            else if (args[0].equals("medium"))
            {
                ItemStack i = this.plugin.bagItems.get(ChatColor.stripColor(this.plugin.bagsEvents.mb_name));
                pp.getInventory().addItem(i);
            }
            else if (args[0].equals("large"))
            {
                ItemStack i = this.plugin.bagItems.get(ChatColor.stripColor(this.plugin.bagsEvents.lb_name));
                pp.getInventory().addItem(i);
            }
            else if (args[0].equals("ender"))
            {
                ItemStack i = this.plugin.bagItems.get(ChatColor.stripColor(this.plugin.bagsEvents.eb_name));
                pp.getInventory().addItem(i);
            }
        }
        else
        {
            pp.sendMessage(this.plugin.error + "You do not have permission for the command.");
        }
    }

    public String name()
    {
        return "give";
    }

    public String info()
    {
        return "give";
    }

    public String[] aliases()
    {
        return new String[] { "g" };
    }
}
