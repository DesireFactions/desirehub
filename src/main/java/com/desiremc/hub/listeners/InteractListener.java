package com.desiremc.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.hub.gui.ServerGUI;
import com.desiremc.hub.session.ServerHandler;

public class InteractListener implements Listener
{

    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getServerSelector()))
            {
                ServerGUI.getInstance().openMenu(e.getPlayer());
            }
        }
    }

}
