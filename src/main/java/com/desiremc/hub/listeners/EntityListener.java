package com.desiremc.hub.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EntityListener implements Listener
{

    @EventHandler
    public void onSpawn(EntitySpawnEvent e)
    {
        if (!(e.getEntity() instanceof Player))
        {
            e.getEntity().remove();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player && !InteractListener.hasPvP((Player) e.getEntity()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e)
    {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player)
        {
            Player target = (Player) e.getEntity();
            Player player = (Player) e.getDamager();

            if (!InteractListener.hasPvP(target) || !InteractListener.hasPvP(player))
            {
                e.setCancelled(true);
            }
        }
        else
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e)
    {
        if (e.getFoodLevel() != 20)
        {
            e.setFoodLevel(20);
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent e)
    {
        InteractListener.removePvP(e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent e)
    {
        InteractListener.removePvP(e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        e.setDeathMessage(null);
    }
}
