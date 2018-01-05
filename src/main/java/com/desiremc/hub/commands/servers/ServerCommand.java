package com.desiremc.hub.commands.servers;

import com.desiremc.core.api.newcommands.ValidBaseCommand;

public class ServerCommand extends ValidBaseCommand
{
    public ServerCommand()
    {
        super("servers", "All server management commands.", new String[]{});

        addSubCommand(new ServerWhitelistCommand());
        addSubCommand(new ServerPartnerWhitelistCommand());
    }
}
