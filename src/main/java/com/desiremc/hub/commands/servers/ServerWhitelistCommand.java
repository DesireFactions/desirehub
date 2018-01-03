package com.desiremc.hub.commands.servers;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.parsers.ServerParser;
import com.desiremc.hub.session.Server;
import com.desiremc.hub.session.ServerHandler;

public class ServerWhitelistCommand extends ValidCommand
{
    public ServerWhitelistCommand()
    {
        super("whitelist", "Toggle whitelist on a server.", Rank.ADMIN, new String[] {});

        addArgument(CommandArgumentBuilder.createBuilder(Server.class)
                .setName("server")
                .setParser(new ServerParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Server server = (Server) args.get(0).getValue();

        server.toggleWhitelisted();
        ServerHandler.getInstance().save(server);

        DesireHub.getLangHandler().sendRenderMessage(sender, "server.whitelisted", true, false, "{server}", sender.getName(), "{status}", server.getWhitelisted());
    }
}
