package com.desiremc.hub.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

    private static List<UUID> inPVP;

    public InteractListener()
    {
        inPVP = new ArrayList<>();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (!event.hasItem() || !event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasDisplayName())
            {
                return;
            }

            if (event.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getServerSelector()))
            {
                if (!ServerGUI.getInstance().hasMenu(player))
                {
                    ServerGUI.getInstance().openMenu(player);
                }
            }
            else if (event.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getPearl()))
            {
                player.setVelocity(player.getEyeLocation().getDirection().multiply(2));
            }
            else if (event.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getInfo()))
            {
                for (String message : DesireHub.getLangHandler().getStringList("info-message"))
                {
                    DesireHub.getLangHandler().sendRenderMessage(player, message, false, true);
                }
            }
            else if (event.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getPvP()))
            {
                inPVP.add(player.getUniqueId());
                player.setGameMode(GameMode.SURVIVAL);
                setInventory(player);
                DesireHub.getLangHandler().sendRenderMessage(player, "pvp.enabled", true, false);
                for (Player target : Bukkit.getOnlinePlayers())
                {
                    if (hasPvP(target))
                    {
                        player.showPlayer(target);
                        target.showPlayer(player);
                    }
                }
            }
            else if (event.getItem().getItemMeta().getDisplayName().equals(ServerHandler.getHider()))
            {
                Session session = SessionHandler.getSession(player);
                if (session.getSetting(SessionSetting.PLAYERS))
                {
                    session.setSetting(SessionSetting.PLAYERS, false);
                    session.save();

                    DesireHub.getLangHandler().sendRenderMessage(player, "players_off", true, false);
                    for (Player target : Bukkit.getOnlinePlayers())
                    {
                        player.showPlayer(target);
                    }
                }
                else
                {
                    session.setSetting(SessionSetting.PLAYERS, true);
                    session.save();

                    DesireHub.getLangHandler().sendRenderMessage(player, "players_on", true, false);
                    for (Player target : Bukkit.getOnlinePlayers())
                    {
                        player.hidePlayer(target);
                    }
                }
            }

            if (!hasPvP(player))
            {
                event.setCancelled(true);
            }
            else
            {
                if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST)
                {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static boolean hasPvP(Player player)
    {
        return inPVP.contains(player.getUniqueId());
    }

    private void setInventory(Player p)
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
        inPVP.remove(player.getUniqueId());

        player.setHealth(20);
    }
}
