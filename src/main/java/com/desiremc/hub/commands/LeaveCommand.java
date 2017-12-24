package com.desiremc.hub.commands;


import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Session;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.listeners.InteractListener;
import com.desiremc.hub.validators.SenderInPVPMode;

import java.util.List;

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
        DesireHub.getLangHandler().sendRenderMessage(sender, "leave.valid");
        InteractListener.removePvP(sender.getPlayer());
    }
}
