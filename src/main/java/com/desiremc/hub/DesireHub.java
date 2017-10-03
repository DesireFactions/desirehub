package com.desiremc.hub;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.hub.session.ServerHandler;

public class DesireHub extends JavaPlugin
{

    private static DesireHub instance;

    private static FileHandler config;
    private static LangHandler lang;

    @Override
    public void onEnable()
    {
        instance = this;

        config = new FileHandler(new File(getDataFolder(), "config.yml"));
        lang = new LangHandler(new File(getDataFolder(), "lang.yml"));

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

    public static FileHandler getConfigHandler()
    {
        return config;
    }

    public static LangHandler getLangHandler()
    {
        return lang;
    }

    public static DesireHub getInstance()
    {
        return instance;
    }

}
