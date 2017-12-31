package com.desiremc.hub.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hub.DesireHub;

public class TeleportCommand extends ValidCommand
{

    public TeleportCommand()
    {
        super("teleport", "Teleport to another player", Rank.HELPER, true, new String[] {"tp"});

        addArgument(CommandArgumentBuilder.createBuilder(Player.class)
                .setName("target")
                .setParser(new PlayerParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Player target = (Player) arguments.get(0).getValue();
        Player player = sender.getPlayer();

        Location loc = target.getLocation();

        loc.setPitch(player.getLocation().getPitch());
        loc.setYaw(player.getLocation().getYaw());

        player.teleport(loc);

        DesireHub.getLangHandler().sendRenderMessage(player, "teleport.self", true, false,
                "{target}", target.getName());
    }

}
