package com.desiremc.hub;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.newcommands.CommandHandler;
import com.desiremc.core.listeners.ListenerManager;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hub.commands.LeaveCommand;
import com.desiremc.hub.commands.TeleportCommand;
import com.desiremc.hub.commands.servers.ServerCommand;
import com.desiremc.hub.commands.spawn.SetSpawnCommand;
import com.desiremc.hub.commands.spawn.SpawnCommand;
import com.desiremc.hub.gui.ServerGUI;
import com.desiremc.hub.handler.TablistHandler;
import com.desiremc.hub.listeners.ChatListener;
import com.desiremc.hub.listeners.ConnectionListener;
import com.desiremc.hub.listeners.EntityListener;
import com.desiremc.hub.listeners.InteractListener;
import com.desiremc.hub.listeners.InventoryListener;
import com.desiremc.hub.listeners.WorldListener;
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

        config = new FileHandler(new File(getDataFolder(), "config.yml"), this);
        lang = new LangHandler(new File(getDataFolder(), "lang.yml"), this);

        registerCommands();
        registerListeners();

        ServerHandler.initialize();
        ServerGUI.loadServers();
        DesireCore.getInstance().getMongoWrapper().getDatastore().ensureIndexes();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        loopBoards();
    }

    private void registerCommands()
    {
        CommandHandler commandHandler = CommandHandler.getInstance();
        commandHandler.registerCommand(new LeaveCommand());
        commandHandler.registerCommand(new SpawnCommand());
        commandHandler.registerCommand(new SetSpawnCommand());
        commandHandler.registerCommand(new TeleportCommand());
        commandHandler.registerCommand(new ServerCommand());
    }

    private void registerListeners()
    {
        ListenerManager listeners = ListenerManager.getInstace();
        listeners.addListener(new ConnectionListener());
        listeners.addListener(new InventoryListener());
        listeners.addListener(new InteractListener());
        listeners.addListener(new EntityListener());
        listeners.addListener(new ChatListener());
        listeners.addListener(new WorldListener());

        listeners.addListener(new TablistHandler());
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

    private void loopBoards()
    {
        Bukkit.getScheduler().runTaskTimer(this, new Runnable()
        {
            @Override
            public void run()
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if (DesireHub.getLangHandler().getBoolean("scoreboard.players.enabled"))
                    {
                        EntryRegistry.getInstance().setValue(player, DesireHub.getLangHandler().renderMessage("scoreboard.players.message", false, false), ServerHandler.getAllPlayers() + "");
                    }

                    if (DesireHub.getLangHandler().getBoolean("scoreboard.rank.enabled"))
                    {
                        EntryRegistry.getInstance().setValue(player, DesireHub.getLangHandler().renderMessage("scoreboard.rank.message", false, false), SessionHandler.getOnlineSession(player.getUniqueId()).getRank().getDisplayName());
                    }

                    if (DesireHub.getLangHandler().getBoolean("scoreboard.server.enabled"))
                    {
                        EntryRegistry.getInstance().setValue(player, DesireHub.getLangHandler().renderMessage("scoreboard.server.message", false, false), DesireCore.getCurrentServer());
                    }

                    if (DesireHub.getLangHandler().getBoolean("scoreboard.queue.enabled"))
                    {
                        EntryRegistry.getInstance().setValue(player, DesireHub.getLangHandler().renderMessage("scoreboard.queue.message", false, false), ServerHandler.getQueuePosition(player));
                    }
                }
            }
        }, 0, 20L);
    }

}
