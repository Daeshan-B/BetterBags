package com.tscforum.betterbags.commands.manager;

import com.tscforum.betterbags.Core;
import com.tscforum.betterbags.commands.GiveBags;
import com.tscforum.betterbags.utils.MessageManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager implements CommandExecutor, Listener {

    private ArrayList<SubCommand> commands = new ArrayList<SubCommand>();
    private Core plugin;

    public CommandManager(Core plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        commands.add(new GiveBags(plugin));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageManager.error + "ONLY PLAYERS CAN USE THE BAGS COMMAND");
            return true;
        }

        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("bags")) {
            if (args.length == 0) {

                p.sendMessage(MessageManager.info + "\nCurrent Version: " + ChatColor.WHITE +
                        plugin.getDescription().getVersion());

                p.sendMessage(ChatColor.YELLOW + "Author:" + ChatColor.WHITE + " TheSourceCode");

                TextComponent message = new TextComponent(ChatColor.GOLD + "Learn to code plugins. Click Here!");
                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                        "https://www.youtube.com/channel/UCNXt2MrZaqfIBknamqwzeXA"));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Bukkit Coding Tutorials").create()));
                p.spigot().sendMessage(message);
                return true;
            }

            SubCommand target = get(args[0]);

            if (target == null) {
                sender.sendMessage(MessageManager.error + "/bags " + args[0] + " is not a valid subcommand!");
                return true;
            }

            ArrayList<String> a = new ArrayList<String>(Arrays.asList(args));
            a.remove(0);
            args = (String[]) a.toArray(new String[0]);
            try {
                target.onCommand(p, args);
            } catch (Exception e) {
                sender.sendMessage(MessageManager.error + e.getCause());
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
