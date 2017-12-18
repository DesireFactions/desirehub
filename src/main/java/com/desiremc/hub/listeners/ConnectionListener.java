package com.desiremc.hub.listeners;

import com.desiremc.core.DesireCore;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.session.ServerHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                    EntryRegistry.getInstance().setValue(p, DesireHub.getLangHandler().renderMessageNoPrefix("scoreboard.players.message"), ServerHandler.getAllPlayers() + "");
                }

                if (DesireHub.getLangHandler().getBoolean("scoreboard.rank.enabled"))
                {
                    EntryRegistry.getInstance().setValue(p, DesireHub.getLangHandler().renderMessageNoPrefix("scoreboard.rank.message"), StringUtils.capitalize(SessionHandler.getSession(p.getUniqueId()).getRank().name().toLowerCase()));
                }

                if (DesireHub.getLangHandler().getBoolean("scoreboard.server.enabled"))
                {
                    EntryRegistry.getInstance().setValue(p, DesireHub.getLangHandler().renderMessageNoPrefix("scoreboard.server.message"), DesireCore.getCurrentServer());
                }

                for (String message : DesireHub.getLangHandler().getStringList("welcome-message"))
                {
                    DesireHub.getLangHandler().sendRenderMessageCenteredNoPrefix(p, message);
                }
            }
        }, 5L);
    }

    private ItemStack[] getItems()
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

        items[4] = compass;
        items[1] = pearl;
        return items;
    }
}
