package com.desiremc.hub.commands.servers;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.parsers.ServerParser;
import com.desiremc.hub.session.Server;
import com.desiremc.hub.session.ServerHandler;

import java.util.List;

public class ServerPartnerWhitelistCommand extends ValidCommand {

    public ServerPartnerWhitelistCommand() {
        super("partner", "Toggle partner whitelist on a server.", Rank.ADMIN, new String[]{});

        addArgument(CommandArgumentBuilder.createBuilder(Server.class)
                .setName("server")
                .setParser(new ServerParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args) {
        Server server = (Server) args.get(0).getValue();

        server.togglePartnerWhitelisted();
        ServerHandler.getInstance().save(server);

        DesireHub.getLangHandler().sendRenderMessage(sender, "server.whitelisted", true, false, "{server}", server.getName(), "{status}", server.getWhitelisted());
    }
}