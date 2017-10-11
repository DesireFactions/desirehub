package com.desiremc.hub.gui;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.DesireCore;
import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.session.Server;
import com.desiremc.hub.session.ServerHandler;

public class ServerGUI extends Menu
{

    private static ServerGUI instance;

    private ServerGUI()
    {
        super(DesireHub.getLangHandler().getString("inventory.title"), 1);

        reloadServers();
    }

    public void reloadServers()
    {
        int i = 0;

        for (Server server : ServerHandler.getServers())
        {
            removeMenuItem(i);
            addMenuItem(new MenuItem(DesireHub.getConfigHandler().getString("servers." + server.getName() + ".item.name"), getItem(server))
            {
                @Override
                public void onClick(Player player)
                {
                    ServerHandler.processPlayer(server, player);
                    reloadServers();
                }
            }, i);
            i++;
        }
    }

    @Override
    public void openMenu(Player p)
    {
        reloadServers();
        super.openMenu(p);
    }

    private ItemStack getItem(Server server)
    {
        ItemStack item = DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("servers." + server.getName() + ".item.type"), 1);

        ItemMeta meta = item.getItemMeta();

        List<String> load = DesireHub.getConfigHandler().getStringList("servers." + server.getName() + ".item.lore");
        List<String> lore = new LinkedList<>();

        for (String str : load)
        {
            lore.add(str.replace("{online}", String.valueOf(server.getOnline())).replace("{slots}", String.valueOf(server.getSlots())).replace("{queue}", String.valueOf(server.getQueue().size())));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static void loadServers()
    {
        instance = new ServerGUI();
    }

    public static ServerGUI getInstance()
    {
        return instance;
    }

}
