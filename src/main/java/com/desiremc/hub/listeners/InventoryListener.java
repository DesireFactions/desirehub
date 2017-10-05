package com.desiremc.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class InventoryListener implements Listener
{

    @EventHandler
    public void onMove(InventoryInteractEvent e)
    {
        e.setCancelled(true);
    }

}
