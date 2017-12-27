package com.desiremc.hub.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.gui.ServerGUI;
import com.desiremc.hub.session.ServerHandler;

public class InteractListener implements Listener
{

    private static List<UUID> pvping;

    public InteractListener()
    {
        pvping = new ArrayList<>();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            e.setCancelled(true);
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
            else if (e.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getInfo()))
            {
                for (String message : DesireHub.getLangHandler().getStringList("info-message"))
                {
                    DesireHub.getLangHandler().sendRenderMessage(e.getPlayer(), message, false, true);
                }
            }
            else if (e.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getPvP()))
            {
                pvping.add(p.getUniqueId());
                p.setGameMode(GameMode.SURVIVAL);
                setInventory(p);
                DesireHub.getLangHandler().sendRenderMessage(p, "pvp.enabled", true, false);
                for (Player target : Bukkit.getOnlinePlayers())
                {
                    if (hasPvP(target))
                    {
                        p.showPlayer(target);
                        target.showPlayer(p);
                    }
                }
            }
            else if (e.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getHider()))
            {
                Session session = SessionHandler.getSession(p);
                if (session.getSetting(SessionSetting.PLAYERS))
                {
                    session.setSetting(SessionSetting.PLAYERS, false);
                    DesireHub.getLangHandler().sendRenderMessage(p, "players_off", true, false);
                }
                else
                {
                    session.setSetting(SessionSetting.PLAYERS, true);
                    DesireHub.getLangHandler().sendRenderMessage(p, "players_on", true, false);
                }
            }
        }
    }

    public static boolean hasPvP(Player player)
    {
        return pvping.contains(player.getUniqueId());
    }

    public void setInventory(Player p)
    {
        ItemStack[] items = new ItemStack[36];
        p.getInventory().setContents(new ItemStack[p.getInventory().getContents().length]);
        p.getInventory().setArmorContents(new ItemStack[p.getInventory().getArmorContents().length]);
        p.updateInventory();

        p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));

        items[0] = new ItemStack(Material.DIAMOND_SWORD);
        for (int i = 1; i < 9; i++)
        {
            items[i] = new ItemStack(Material.POTION, 1, (short) 16421);
        }

        p.getInventory().setContents(items);
        p.updateInventory();
    }

    public static void removePvP(Player player)
    {
        player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
        player.getInventory().setContents(ConnectionListener.getItems());

        player.updateInventory();
        pvping.remove(player.getUniqueId());
    }
}
