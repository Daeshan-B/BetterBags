package com.tscforum.betterbags;

import com.tscforum.betterbags.commands.manager.CommandManager;
import com.tscforum.betterbags.listeners.BagsEventsClass;
import com.tscforum.betterbags.listeners.BagsRecipeClass;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class BagMainClass extends JavaPlugin {

    public String error = ChatColor.DARK_RED  + "#BAGS ERROR " + ChatColor.RED;
    public String info = ChatColor.GOLD + "#BAGS INFO " + ChatColor.YELLOW;
    public String good = ChatColor.DARK_GREEN  + "#BAGS " + ChatColor.GREEN;

    public ArrayList<Inventory> bags = new ArrayList<>();
    public HashMap<String, ItemStack> bagItems = new HashMap<>();
    public BagsEventsClass bagsEvents;
    public BagsRecipeClass bagsRecipe;

    public void onEnable() {
        this.bagsEvents = new BagsEventsClass();
        this.bagsRecipe = new BagsRecipeClass();
        this.bagsEvents.createbags();

        eventReg();
        loadConfigs();

        CommandManager commands = new CommandManager();
        getCommand("bags").setExecutor(commands);
        commands.setup();
    }

    public void onDisable() {
        this.bags.clear();
        this.bagItems.clear();
    }

    private void eventReg() {
        getServer().getPluginManager().registerEvents(new BagsEventsClass(), this);
    }

    private void loadConfigs() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}