package com.daeshan.betterbags.commands;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.commands.manager.SubCommand;
import com.daeshan.betterbags.utils.MessageManager;
import org.bukkit.entity.Player;
import org.dizitart.no2.tool.Exporter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportNitrite extends SubCommand {

    private BetterBagsCore plugin;

    public ExportNitrite(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    public void onCommand(Player player, String[] args) {

        if ((player.hasPermission("bags.export")) || player.isOp()) {
            File path = new File(plugin.getDataFolder() + "/export");
            if(!path.exists()){
                path.mkdirs();
            }
            Exporter exporter = Exporter.of(plugin.nitRiteData.database);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date();
            exporter.exportTo(path + "/" + dateFormat.format(date) + ".json");
            MessageManager.infoPlayer(player, "You have exported a JSON file. Please view this in any program that accept JSON files.");
        }
    }

    public String name() {
        return "export";
    }

    public String info() {
        return "Export nitrite database data to an sql file.";
    }

    public String[] aliases() {
        return new String[]{"ex"};
    }
}
