package com.desiremc.hub.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.desiremc.hub.DesireHub;

public class WorldListener implements Listener
{

    public WorldListener()
    {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DesireHub.getInstance(), new Runnable()
        {
            public void run()
            {
                for (World w : Bukkit.getServer().getWorlds())
                {
                    w.setTime(0L);
                }
            }
        }, 0L, 10000L);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event)
    {
        event.setCancelled(event.toWeatherState());
    }
}
