package com.desiremc.hub.validators;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.listeners.InteractListener;

public class SenderInPVPMode implements SenderValidator
{
    @Override
    public boolean validate(Session sender)
    {
        if (!InteractListener.hasPvP(sender.getPlayer()))
        {
            DesireHub.getLangHandler().sendRenderMessage(sender, "leave.invalid", true, false);
            return false;
        }
        return true;
    }
}
