package com.daeshan.betterbags;

import com.daeshan.betterbags.bagutils.BagManager;
import com.daeshan.betterbags.bagutils.BagRecipes;
import com.daeshan.betterbags.commands.manager.CommandManager;
import com.daeshan.betterbags.listeners.BagCloseEvent;
import com.daeshan.betterbags.listeners.BagOpenEvent;
import com.daeshan.betterbags.listeners.CraftBagsEvent;
import com.daeshan.betterbags.listeners.SetupPlayerEvent;
import com.daeshan.betterbags.utils.NitRiteData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class BetterBagsCore extends JavaPlugin {

    public HashMap<String, ItemStack> bagItems;
    public SetupPlayerEvent setupPlayer;
    public CraftBagsEvent craftBagsEvent;
    public BagOpenEvent openBagEvent;
    public BagCloseEvent bagCloseEvent;

    public CommandManager commandManager;
    public BagManager bagManager;
    public BagRecipes bagRecipes;
    public NitRiteData nitRiteData;

    @Override
    public void onEnable() {
        loadConfigs();
        bagItems = new HashMap<>();
        bagRecipes = new BagRecipes(this);


        nitRiteData = new NitRiteData(this);
        bagManager = new BagManager(this);
        craftBagsEvent = new CraftBagsEvent(this);
        openBagEvent = new BagOpenEvent(this);
        bagCloseEvent = new BagCloseEvent(this);
        setupPlayer = new SetupPlayerEvent(this);
        commandManager = new CommandManager(this);

        nitRiteData.setup();
        bagRecipes.setupRecipes();
        commandManager.setup();
        eventReg();
    }

    @Override
    public void onDisable() {
        bagItems.clear();
        nitRiteData.closeConnection();
    }

    private void eventReg() {

        getServer().getPluginManager().registerEvents(setupPlayer, this);
        getServer().getPluginManager().registerEvents(openBagEvent, this);
        getServer().getPluginManager().registerEvents(bagCloseEvent, this);
        getServer().getPluginManager().registerEvents(craftBagsEvent, this);
    }

    private void loadConfigs() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}