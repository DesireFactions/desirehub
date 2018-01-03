package com.desiremc.hub.commands.servers;

import com.desiremc.core.api.newcommands.ValidBaseCommand;

public class ServerCommand extends ValidBaseCommand
{
    public ServerCommand()
    {
        super("server", "All server management commands.", new String[] {"servers"});

        addSubCommand(new ServerWhitelistCommand());
    }
}
