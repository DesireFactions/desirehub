package com.desiremc.hub.commands;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.listeners.InteractListener;
import com.desiremc.hub.validators.SenderInPVPMode;

public class LeaveCommand extends ValidCommand
{
    public LeaveCommand()
    {
        super("leave", "Leave PvP mode", true);

        addSenderValidator(new SenderInPVPMode());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        DesireHub.getLangHandler().sendRenderMessage(sender, "leave.valid", true, false);
        InteractListener.removePvP(sender.getPlayer());

        if (sender.getSetting(SessionSetting.PLAYERS))
        {
            for (Player target : Bukkit.getOnlinePlayers())
            {
                sender.getPlayer().hidePlayer(target);
            }
        }
    }
}
