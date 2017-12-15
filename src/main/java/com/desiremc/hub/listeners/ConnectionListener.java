package com.desiremc.hub.listeners;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.tablist.TabAPI;
import com.desiremc.core.tablist.TabList;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.session.ServerHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConnectionListener implements Listener
{

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        p.getInventory().setContents(getItems());
        Bukkit.getScheduler().runTaskLater(DesireHub.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (Session s : SessionHandler.getSessions())
                {
                    if (s.getSetting(SessionSetting.CLASSICTAB))
                    {
                        applyClassic(s.getPlayer());
                    }
                    else
                    {
                        applyHub(s.getPlayer());
                    }
                }

                /*for (String s : DesireHub.getLangHandler().getStringList("scoreboard"))
                {
                    Bukkit.broadcastMessage(s);
                    EntryRegistry.getInstance().setValue(p, DesireHub.getLangHandler().renderString(s, "{player}", p.getName(), "{current}", Bukkit.getServer().getOnlinePlayers().size(), "{max}", Bukkit.getMaxPlayers()), " ");
                }*/

                for (String message : DesireHub.getLangHandler().getStringList("welcome-message"))
                {
                    DesireHub.getLangHandler().sendRenderMessageCenteredNoPrefix(p, message);
                }
            }
        }, 5L);
    }

    private ItemStack[] getItems()
    {
        ItemStack[] items = new ItemStack[28];

        ItemStack compass = DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("selector.item"), 1);

        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(ServerHandler.getServerSelector());
        compass.setItemMeta(meta);

        ItemStack pearl = DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("pearl.item"), 1);

        ItemMeta pearlMeta = compass.getItemMeta();
        pearlMeta.setDisplayName(ServerHandler.getPearl());
        pearl.setItemMeta(pearlMeta);

        items[4] = compass;
        items[1] = pearl;
        return items;
    }

    private void applyClassic(Player player)
    {
        TabList list = getTabList(player);
        if (!list.isOld())
        {
            return;
        }
        list.clearAllSlots();

        int i = 0;
        for (Session s : SessionHandler.getSessions())
        {
            list.setSlot(i, s.getName());
            String prefix = null, name, suffix = "";
            String str = s.getRank().getColor() + s.getName();

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

    }

    private void applyHub(Player player)
    {
        applyClassic(player);
    }

    private static TabList getTabList(Player player)
    {
        TabList list = TabAPI.getPlayerTabList(player);
        if (list == null)
        {
            list = TabAPI.createTabListForPlayer(player);
            list.send();
        }
        return list;
    }

}
