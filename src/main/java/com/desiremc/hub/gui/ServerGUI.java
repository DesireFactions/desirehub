package com.desiremc.hub.gui;

import com.desiremc.core.DesireCore;
import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.session.Server;
import com.desiremc.hub.session.ServerHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class ServerGUI extends Menu
{

    private static ServerGUI instance;

    private ServerGUI()
    {
        super(DesireHub.getLangHandler().renderMessage("inventory.title", false, false), 3);

        reloadServers();
    }

    public void reloadServers()
    {
        Server first = ServerHandler.getServer("first");

        removeMenuItem(getSlot(first));
        addMenuItem(new MenuItem(DesireHub.getConfigHandler().getString("servers.first.item.name"), getItem(first))
        {
            @Override
            public void onClick(Player player)
            {
                ServerHandler.processPlayer(ServerHandler.getServer("first"), player);
                reloadServers();
            }
        }, getSlot(first));

        Inventory inv = super.getInventory();

        for (int slot = 0; slot < inv.getSize(); slot++)
        {
            ItemStack item = inv.getItem(slot);
            if (item == null || item.getType().equals(Material.AIR))
            {
                ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                ItemMeta meta = pane.getItemMeta();

                meta.setDisplayName(ChatColor.RESET + "");

                pane.setItemMeta(meta);
                inv.setItem(slot, pane);
            }
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

    private int getSlot(Server server)
    {
        return DesireHub.getConfigHandler().getInteger("servers." + server.getName() + ".item.slot");
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
