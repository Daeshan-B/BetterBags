package com.tscforum.betterbags;

import com.tscforum.betterbags.utils.MessageManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.*;

public class BagsConfigManager implements Listener {

    public File bags;
    public FileConfiguration bagsconfig;
    private Core plugin = (Core) Core.getPlugin(Core.class);

    public void create() {
        this.bags = new File(this.plugin.getDataFolder(), "User_Bag_Data.yml");
        this.bagsconfig = new YamlConfiguration();
        if (this.bags.exists()) {
            this.plugin.getServer().getConsoleSender().sendMessage(MessageManager.error + "User_Bag_Data.yml already exists. Creation was skipped!");
        } else {
            try {
                this.bagsconfig.options().copyDefaults();
                this.bagsconfig.save(this.bags);
                this.plugin.getServer().getConsoleSender().sendMessage(MessageManager.info + "User_Bag_Data.yml Created!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() throws UnsupportedEncodingException {

        if (this.bags == null) {
            this.bags = new File(this.plugin.getDataFolder(), "User_Bag_Data.yml");
        }
        this.bagsconfig = YamlConfiguration.loadConfiguration(this.bags);

        Reader defConfigStream = new InputStreamReader(this.plugin.getResource("User_Bag_Data.yml.yml"), "UTF8");
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        bagsconfig.setDefaults(defConfig);
    }
}
