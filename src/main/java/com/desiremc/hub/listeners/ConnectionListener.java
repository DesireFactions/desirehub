package com.desiremc.hub.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.DesireCore;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.session.ServerHandler;

public class ConnectionListener implements Listener
{

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        p.getInventory().setContents(getItems());
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

}
