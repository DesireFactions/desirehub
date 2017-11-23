package com.desiremc.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryListener implements Listener
{

    @EventHandler
    public void onMove(InventoryInteractEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event)
    {
        event.setCancelled(true);
    }

}
