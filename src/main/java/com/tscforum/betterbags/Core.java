package com.tscforum.betterbags;

import com.tscforum.betterbags.commands.manager.CommandManager;
import com.tscforum.betterbags.listeners.BagsEventsClass;
import com.tscforum.betterbags.recipes.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class Core extends JavaPlugin {

    public ArrayList<Inventory> bags = new ArrayList<>();
    public HashMap<String, ItemStack> bagItems = new HashMap<>();
    public BagsEventsClass bagsEvents;

    public EnderBagRecipe enderBagRecipe;
    public LargeBagRecipe largeBagRecipe;
    public MediumBagRecipe mediumBagRecipe;
    public SmallBagRecipe smallBagRecipe;

    @Override
    public void onEnable() {
        bagsEvents = new BagsEventsClass(this);

        enderBagRecipe = new EnderBagRecipe(this);
        largeBagRecipe = new LargeBagRecipe(this);
        mediumBagRecipe = new MediumBagRecipe(this);
        smallBagRecipe = new SmallBagRecipe(this);


        bagsEvents.createbags();

        eventReg();
        loadConfigs();

        CommandManager commands = new CommandManager(this);
        getCommand("bags").setExecutor(commands);
        commands.setup();
    }

    @Override
    public void onDisable() {
        bags.clear();
        bagItems.clear();
    }

    private void eventReg() {
        getServer().getPluginManager().registerEvents(new BagsEventsClass(this), this);
    }

    private void loadConfigs() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}