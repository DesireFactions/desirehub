package com.desiremc.hub.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

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
        if (e.getEntity() instanceof Player)
        {
            e.setCancelled(true);
        }
    }

}
