package com.desiremc.hub;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.listeners.ListenerManager;
import com.desiremc.hub.gui.ServerGUI;
import com.desiremc.hub.listeners.ConnectionListener;
import com.desiremc.hub.listeners.EntityListener;
import com.desiremc.hub.listeners.InteractListener;
import com.desiremc.hub.listeners.InventoryListener;
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

        saveDefaultConfig();
        saveResource("lang.yml", false);

        config = new FileHandler(new File(getDataFolder(), "config.yml"));
        lang = new LangHandler(new File(getDataFolder(), "lang.yml"));

        registerCommands();
        registerListeners();

        ServerHandler.initialize();
        ServerGUI.loadServers();
        
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void registerCommands()
    {

    }

    private void registerListeners()
    {
        ListenerManager listeners = ListenerManager.getInstace();
        listeners.addListener(new ConnectionListener());
        listeners.addListener(new InventoryListener());
        listeners.addListener(new InteractListener());
        listeners.addListener(new EntityListener());
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
