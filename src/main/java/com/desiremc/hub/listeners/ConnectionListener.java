package com.desiremc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.tablist.Entry;
import com.desiremc.core.tablist.Tab;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.session.ServerHandler;

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
                for (Session s : SessionHandler.getInstance().getSessions())
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
            }
        }, 5l);
    }

    private ItemStack[] getItems()
    {
        ItemStack[] items = new ItemStack[28];

        ItemStack compass = DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("selector.item"), 1);

        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(ServerHandler.getServerSelector());
        compass.setItemMeta(meta);

        items[4] = compass;
        return items;
    }

    @SuppressWarnings("deprecation")
    private void applyClassic(Player player)
    {
        Tab tab = Tab.getByPlayer(player);
        Entry entry;
        int i = 0;
        for (Player p : Bukkit.getOnlinePlayers())
        {
            entry = tab.getEntry(i / 20, i % 20);
            entry.setText(p.getName()).send();
            i++;
        }
    }

    private void applyHub(Player player)
    {
        applyClassic(player);
    }

}
