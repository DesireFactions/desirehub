package com.desiremc.hub.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.DesireCore;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.commands.spawn.SpawnCommand;
import com.desiremc.hub.session.Server;
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
                if (DesireHub.getLangHandler().getBoolean("scoreboard.players.enabled"))
                {
                    EntryRegistry.getInstance().setValue(p, DesireHub.getLangHandler().renderMessage("scoreboard.players.message", false, false), ServerHandler.getAllPlayers() + "");
                }

                if (DesireHub.getLangHandler().getBoolean("scoreboard.rank.enabled"))
                {
                    EntryRegistry.getInstance().setValue(p, DesireHub.getLangHandler().renderMessage("scoreboard.rank.message", false, false), StringUtils.capitalize(SessionHandler.getOnlineSession(p.getUniqueId()).getRank().name().toLowerCase()));
                }

                if (DesireHub.getLangHandler().getBoolean("scoreboard.server.enabled"))
                {
                    EntryRegistry.getInstance().setValue(p, DesireHub.getLangHandler().renderMessage("scoreboard.server.message", false, false), DesireCore.getCurrentServer());
                }

                for (String message : DesireHub.getLangHandler().getStringList("welcome-message"))
                {
                    DesireHub.getLangHandler().sendRenderMessage(p, message, false, true);
                }
            }
        }, 5L);

        Session session = SessionHandler.getSession(p);

        if (session.getSetting(SessionSetting.PLAYERS))
        {
            for (Player target : Bukkit.getOnlinePlayers())
            {
                p.hidePlayer(target);
            }
        }

        for (Session target : SessionHandler.getOnlineSessions())
        {
            if (target.getSetting(SessionSetting.PLAYERS))
            {
                target.getPlayer().hidePlayer(p);
            }
        }

        Bukkit.getScheduler().runTaskLater(DesireHub.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                p.teleport(SpawnCommand.getInstance().getSpawn());
            }
        }, 5L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Session session = SessionHandler.getOnlineSession(event.getPlayer().getUniqueId());

        for (Server server : ServerHandler.getServers())
        {
            server.removeFromQueue(session);
        }
    }

    public static ItemStack[] getItems()
    {
        ItemStack[] items = new ItemStack[28];

        ItemStack compass = DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("selector.item"), 1);

        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName(ServerHandler.getServerSelector());
        compass.setItemMeta(meta);

        ItemStack pearl = DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("pearl.item"), 1);

        ItemMeta pearlMeta = compass.getItemMeta();
        pearlMeta.setDisplayName(ServerHandler.getPearl());
        pearl.setItemMeta(pearlMeta);

        ItemStack shop = DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("info.item"), 1);

        ItemMeta shopMeta = shop.getItemMeta();
        shopMeta.setDisplayName(ServerHandler.getInfo());
        shop.setItemMeta(shopMeta);

        ItemStack pvp = DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("pvp.item"), 1);

        ItemMeta pvpMeta = pvp.getItemMeta();
        pvpMeta.setDisplayName(ServerHandler.getPvP());
        pvp.setItemMeta(pvpMeta);

        ItemStack hider = DesireCore.getItemHandler().get(DesireHub.getConfigHandler().getString("hider.item"), 1);

        ItemMeta hiderMeta = hider.getItemMeta();
        hiderMeta.setDisplayName(ServerHandler.getHider());
        hider.setItemMeta(hiderMeta);

        items[4] = compass;
        items[0] = pearl;
        items[8] = shop;
        items[2] = pvp;
        items[6] = hider;
        return items;
    }
}
