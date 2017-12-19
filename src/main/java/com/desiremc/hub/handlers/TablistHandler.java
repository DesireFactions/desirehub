package com.desiremc.hub.handlers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.tablist.TabAPI;
import com.desiremc.core.tablist.TabList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Iterator;

public class TablistHandler implements Listener
{

    private static final boolean DEBUG = false;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        if (DEBUG)
        {
            System.out.println("TablistHandler.onJoin() called.");
        }
        Session iterSession;
        for (Iterator<Session> it = SessionHandler.getSessions().iterator(); it.hasNext(); )
        {
            iterSession = it.next();
            if (DEBUG)
            {
                System.out.println("TablistHandler.onJoin() iterated with " + iterSession.getName());
            }
            if (iterSession.getPlayer() == null || !iterSession.getPlayer().isOnline())
            {
                if (DEBUG)
                {
                    System.out.println("TablistHandler.onJoin() removed offline or null player.");
                }
                it.remove();
                continue;
            }

            applyClassic(iterSession.getPlayer(), null);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Session iterSession;
        for (Iterator<Session> it = SessionHandler.getSessions().iterator(); it.hasNext(); )
        {
            iterSession = it.next();
            if (iterSession.getPlayer() == null || !iterSession.getPlayer().isOnline())
            {
                it.remove();
                continue;
            }
            if (event.getPlayer() != iterSession.getPlayer())
            {
                applyClassic(iterSession.getPlayer(), event.getPlayer());
            }
        }
        TabAPI.removePlayer(event.getPlayer());
    }

    private void applyClassic(Player player, Player ignored)
    {
        Bukkit.getScheduler().runTaskAsynchronously(DesireCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                // get or initialize the player's tab list
                TabList list = getTabList(player);

                // if the player is on 1.8, ignore them
                if (!list.isOld())
                {
                    return;
                }

                // get the faction's player store
                Session user = SessionHandler.getOnlineSession(player.getUniqueId());
                if (user == null)
                {
                    return;
                }

                // remove all existing player slots. This is annoying to do, but necessary for now.
                list.clearAllSlots();

                // used to count up the player slots
                int i = 0;

                // set all online players.
                for (Player online : Bukkit.getOnlinePlayers())
                {
                    Session session = SessionHandler.getOnlineSession(online.getUniqueId());

                    if (session.getPlayer() == ignored)
                    {
                        continue;
                    }
                    String prefix = null, name, suffix = "";
                    String str = session.getName();

                    if (str.length() <= 16)
                    {
                        name = str;
                    }
                    else if (str.length() > 16 && str.length() <= 32 && str.charAt(15) != '§')
                    {
                        prefix = str.substring(0, str.length() - 16);
                        name = str.substring(str.length() - 16);
                    }
                    else
                    {
                        prefix = str.substring(0, 16);
                        name = str.substring(16, 32);
                        suffix = str.substring(32);
                    }
                    if (prefix != null)
                    {
                        list.setSlot(i, prefix, name, suffix);
                    }
                    else
                    {
                        list.setSlot(i, name);
                    }
                    i++;
                }

                // update the player list
                list.update();
            }
        });
    }

    private static TabList getTabList(Player player)
    {
        if (DEBUG)
        {
            System.out.println("TablistHandler.getTabList(Player) called.");
        }
        TabList list = TabAPI.getPlayerTabList(player);
        if (list == null)
        {
            if (DEBUG)
            {
                System.out.println("TablistHandler.getTabList(Player) it's null, populate.");
            }
            list = TabAPI.createTabListForPlayer(player);
            list.send();
        }
        if (DEBUG)
        {
            System.out.println("TablistHandler.getTabList(Player) return the list.");
        }
        return list;
    }

}
