package com.desiremc.hub.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.DesireCore;
import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.session.Server;
import com.desiremc.hub.session.ServerHandler;

public class ServerGUI extends Menu
{

    public ServerGUI()
    {
        super(DesireHub.getConfigHandler().getString("inventory.title"), 1);

        loadServers();
    }

    private void loadServers()
    {
        int i = 0;
        for (Server server : ServerHandler.getInstance().getServers())
        {
            addMenuItem(new MenuItem(server.getName(), getItem(server))
            {

                @Override
                public void onClick(Player player)
                {
                    // TODO redirect them to the right server
                }
            }, i);
            i++;
        }
    }

    private ItemStack getItem(Server server)
    {
        return new ItemStack(DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("servers." + server.getName() + ".item")));
    }

}
