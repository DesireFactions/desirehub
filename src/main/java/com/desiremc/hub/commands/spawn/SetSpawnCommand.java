package com.desiremc.hub.commands.spawn;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hub.DesireHub;

public class SetSpawnCommand extends ValidCommand
{

    public SetSpawnCommand()
    {
        super("setspawn", "Set the server spawn.", Rank.ADMIN, true);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player p = sender.getPlayer();
        FileHandler config = DesireHub.getConfigHandler();
        Location loc = p.getLocation();
        Bukkit.broadcastMessage("Name: " + p.getName());
        Bukkit.broadcastMessage("World: " + p.getLocation().getWorld().getName());

        config.setDouble("spawn.x", loc.getX());
        config.setDouble("spawn.y", loc.getY());
        config.setDouble("spawn.z", loc.getZ());
        config.setDouble("spawn.pitch", loc.getPitch());
        config.setDouble("spawn.yaw", loc.getYaw());
        config.setString("spawn.world", loc.getWorld().getName());

        SpawnCommand.getInstance().setSpawn(loc);

        DesireHub.getLangHandler().sendRenderMessage(sender, "spawn.set", true, false);
    }
}
