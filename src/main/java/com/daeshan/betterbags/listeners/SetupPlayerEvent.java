package com.daeshan.betterbags.listeners;

import com.daeshan.betterbags.BetterBagsCore;
import com.daeshan.betterbags.utils.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SetupPlayerEvent implements Listener {

    private final BetterBagsCore plugin;

    public SetupPlayerEvent(BetterBagsCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoinServer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.nitRiteData.createPlayer(player);

        MessageManager.goodPlayer(player,
                plugin.nitRiteData.getFromDocument("uuid", player.getUniqueId().toString(), "bag.count").toString());

    }
}
