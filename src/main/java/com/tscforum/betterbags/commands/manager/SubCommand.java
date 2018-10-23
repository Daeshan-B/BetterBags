package com.tscforum.betterbags.commands.manager;


import org.bukkit.entity.Player;

public abstract class SubCommand
{
    public abstract void onCommand(Player paramPlayer, String[] paramArrayOfString);

    public abstract String name();

    public abstract String info();

    public abstract String[] aliases();
}
