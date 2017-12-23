package com.desiremc.hub.listeners;

import com.desiremc.hub.DesireHub;
import com.desiremc.hub.gui.ServerGUI;
import com.desiremc.hub.session.ServerHandler;
import org.bukkit.entity.EnderPearl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener
{

    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (!e.hasItem() || !e.getItem().hasItemMeta() || !e.getItem().getItemMeta().hasDisplayName())
            {
                return;
            }

            if (e.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getServerSelector()))
            {
                ServerGUI.getInstance().openMenu(e.getPlayer());
            }
            else if (e.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getPearl()))
            {
                e.getPlayer().launchProjectile(EnderPearl.class);
            }
            else if (e.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getShop()))
            {
                for (String message : DesireHub.getLangHandler().getStringList("shop-message"))
                {
                    DesireHub.getLangHandler().sendRenderMessageCenteredNoPrefix(e.getPlayer(), message);
                }
            }
        }
    }

}
