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
import com.desiremc.hub.session.ServerHandler;

public class TablistHandler implements Listener
{
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
                tabList.setSlot(1, 1, "§bOnline" + ServerHandler.getAllPlayers());

                tabList.setSlot(3, 0, "§b§lStore");
                tabList.setSlot(4, 0, "§bstore.desirehcf.com");

                tabList.setSlot(6, 0, "§b§lTeamSpeak");
                tabList.setSlot(7, 0, "§bts.desirehcf.com");

                tabList.setSlot(9, 0, "§b§lReddit");
                tabList.setSlot(10, 0, "§breddit.com/r/desiremc");

                tabList.setSlot(12, 0, "§b§lDiscord");
                tabList.setSlot(13, 0, "§bdiscord.desirehcf.com");
            }
            else
            {
                tabList = TabAPI.getPlayerTabList(session.getPlayer());
                tabList.setSlot(6, 2, "§7" + Bukkit.getOnlinePlayers().size());
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
                tabList.setSlot(6, 2, "§7" + Bukkit.getOnlinePlayers().size());
            }
        }
        TabAPI.removePlayer(event.getPlayer());
    }
}
