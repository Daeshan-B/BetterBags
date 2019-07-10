package com.daeshan.betterbags.commands.manager;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.commands.ExportNitrite;
import com.daeshan.betterbags.commands.GiveBags;
import com.daeshan.betterbags.utils.MessageManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager implements CommandExecutor {

    private ArrayList<SubCommand> commands = new ArrayList<>();
    private BetterBagsCore plugin;

    public CommandManager(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        plugin.getCommand("bags").setExecutor(this);
        commands.add(new GiveBags(plugin));
        commands.add(new ExportNitrite(plugin));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.errorConsole("ONLY PLAYERS CAN USE THE BAGS COMMAND");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("bags")) {
            if (args.length == 0) {

                MessageManager.infoPlayer(player, "\nCurrent Version: " + ChatColor.GOLD +
                        plugin.getDescription().getVersion());

                MessageManager.infoPlayer(player, "Author:" + ChatColor.GOLD + " TheSourceCode");

                TextComponent message = new TextComponent(ChatColor.GOLD + "Learn to code plugins. Click Here!");
                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                        "https://www.youtube.com/channel/UCNXt2MrZaqfIBknamqwzeXA"));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Bukkit Coding Tutorials").create()));
                player.spigot().sendMessage(message);
                return true;
            }

            SubCommand target = get(args[0]);

            if (target == null) {
                MessageManager.errorPlayer(player, "/bags " + args[0] + " is not a valid subcommand!");
                return true;
            }

            ArrayList<String> a = new ArrayList<>(Arrays.asList(args));
            a.remove(0);
            args = a.toArray(new String[0]);
            try {
                target.onCommand(player, args);
            } catch (Exception e) {
                MessageManager.errorConsole("" + e.getCause());
                e.printStackTrace();
            }
        }
        return true;
    }

    private SubCommand get(String name) {

        for (SubCommand sc : commands) {
            if (sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases;
            int length = (aliases = sc.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    return sc;
                }

            }
        }
        return null;
    }
}
