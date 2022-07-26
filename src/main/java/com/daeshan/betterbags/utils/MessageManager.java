package com.daeshan.betterbags.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {

    public static void goodPlayer(Player player, String message) {
        player.sendMessage(ChatColor.GREEN + "#BAGS: " + ChatColor.GRAY + message);
    }

    public static void infoPlayer(Player player, String message) {
        player.sendMessage(ChatColor.GOLD + "#BAGS-INFO: " + ChatColor.GRAY + message);
    }

    public static void errorPlayer(Player player, String message) {
        player.sendMessage(ChatColor.RED + "#BAGS-ERROR: " + ChatColor.GRAY + message);
    }

    public static void debug(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "#BAGS-DEBUG: " + ChatColor.WHITE + message);
    }

    public static void errorConsole(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "#BAGS-ERROR: " + ChatColor.WHITE + message);
    }
}
