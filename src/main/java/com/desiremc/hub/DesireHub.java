package com.desiremc.hub;

import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.hub.session.ServerHandler;

public class DesireHub extends JavaPlugin
{

    private static DesireHub instance;

    @Override
    public void onEnable()
    {
        instance = this;

        registerCommands();
        registerListeners();
        
        ServerHandler.initialize();
    }

    private void registerCommands()
    {

    }

    private void registerListeners()
    {

    }

    public static DesireHub getInstance()
    {
        return instance;
    }

}
