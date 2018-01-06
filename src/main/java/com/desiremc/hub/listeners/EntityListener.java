package com.desiremc.hub.listeners;

import com.desiremc.core.staff.StaffHandler;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.commands.spawn.SpawnCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class EntityListener implements Listener
{

    @EventHandler
    public void onSpawn(EntitySpawnEvent event)
    {
        if (!(event.getEntity() instanceof Player))
        {
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player && !InteractListener.hasPvP((Player) event.getEntity()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            Player target = (Player) event.getEntity();
            Player player = (Player) event.getDamager();

            if (!InteractListener.hasPvP(target) || !InteractListener.hasPvP(player) ||
                    StaffHandler.getInstance().isFrozen(target) || StaffHandler.getInstance().isFrozen(player))
            {
                event.setCancelled(true);
            }
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event)
    {
        if (event.getFoodLevel() != 20)
        {
            event.setFoodLevel(20);
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event)
    {
        InteractListener.removePvP(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event)
    {
        Player player = event.getPlayer();
        InteractListener.removePvP(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(DesireHub.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                player.teleport(SpawnCommand.getInstance().getSpawn());
            }
        }, 5L);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        int x = Math.abs(event.getPlayer().getLocation().getBlockX());
        int z = Math.abs(event.getPlayer().getLocation().getBlockZ());

        if (event.getTo().getY() > 0 || (x < 200 && z < 200))
        {
            return;
        }

        Player player = event.getPlayer();
        player.teleport(SpawnCommand.getInstance().getSpawn());
    }
}
