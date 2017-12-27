package com.desiremc.hub.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.tablist.TabAPI;
import com.desiremc.core.tablist.TabList;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.session.ServerHandler;

public class TablistHandler implements Listener
{

    public TablistHandler()
    {
        Bukkit.getScheduler().runTaskTimer(DesireHub.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    TabList tabList = TabAPI.getPlayerTabList(p);

                    tabList.setSlot(1, 1, "§bOnline: " + ServerHandler.getAllPlayers());
                    tabList.setSlot(11, 0, "§fOnline: " + ServerHandler.getServer("first").getOnline());
                }
            }
        }, 0, 20);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        Player joiner = event.getPlayer();
        for (Session session : SessionHandler.getOnlineSessions())
        {
            TabList tabList;
            if (event.getPlayer().getUniqueId().equals(session.getUniqueId()))
            {
                tabList = TabAPI.createTabListForPlayer(joiner);

                tabList.setSlot(0, 1, "§3§lDesireHCF");
                tabList.setSlot(1, 1, "§fOnline: " + ServerHandler.getAllPlayers());

                tabList.setSlot(3, 1, "§b§lUseful links");

                tabList.setSlot(4, 0, "§b§lStore");
                tabList.setSlot(5, 0, "§fbit.ly/2l3mL7t");

                tabList.setSlot(4, 2, "§b§lTeamSpeak");
                tabList.setSlot(5, 2, "§fbit.ly/2C9zhMp");

                tabList.setSlot(6, 0, "§b§lReddit");
                tabList.setSlot(7, 0, "§fbit.ly/2pCsi9P");

                tabList.setSlot(6, 2, "§b§lDiscord");
                tabList.setSlot(7, 2, "§fbit.ly/2DRhlEp");

                tabList.setSlot(9, 1, "§b§lServer Info");

                tabList.setSlot(10, 0, "§b§lAlpha");
                tabList.setSlot(11, 0, "§fOnline: " + ServerHandler.getServer("first").getOnline());

                tabList.setSlot(13, 1, "§b§lPlayer Info");
                tabList.setSlot(14, 0, "§bRank:");
                tabList.setSlot(15, 0, "§f" + StringUtils.capitalize(session.getRank().name().toLowerCase()));
            }
            else
            {
                tabList = TabAPI.getPlayerTabList(session.getPlayer());
                tabList.setSlot(1, 1, "§fOnline: " + ServerHandler.getAllPlayers());
                tabList.setSlot(11, 0, "§fOnline: " + ServerHandler.getServer("first").getOnline());
            }
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player leaver = event.getPlayer();
        TabList tabList;
        for (Session session : SessionHandler.getOnlineSessions())
        {
            tabList = TabAPI.getPlayerTabList(session.getPlayer());
            if (session.getPlayer() == leaver)
            {
                tabList.terminate();
            }
            else
            {
                tabList = TabAPI.getPlayerTabList(session.getPlayer());
                tabList.setSlot(1, 1, "§bOnline: " + ServerHandler.getAllPlayers());
                tabList.setSlot(11, 0, "§fOnline: " + ServerHandler.getServer("first").getOnline());
            }
        }
        TabAPI.removePlayer(event.getPlayer());
    }
}
