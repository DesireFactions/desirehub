package com.desiremc.hub.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryListener implements Listener
{

    @EventHandler
    public void onMove(InventoryInteractEvent e)
    {
        if (!(e.getWhoClicked() instanceof Player))
        {
            e.setCancelled(true);
            return;
        }

        Player player = (Player) e.getWhoClicked();

        if (!InteractListener.hasPvP(player))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event)
    {
        event.setCancelled(true);
    }

}
