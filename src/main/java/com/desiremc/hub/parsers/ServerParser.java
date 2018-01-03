package com.desiremc.hub.parsers;

import java.util.ArrayList;
import java.util.List;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.session.Server;
import com.desiremc.hub.session.ServerHandler;

public class ServerParser implements Parser<Server>
{
    @Override
    public Server parseArgument(Session sender, String[] label, String rawArgument)
    {
        Server server = ServerHandler.getServer(rawArgument);

        if (server == null)
        {
            DesireHub.getLangHandler().sendRenderMessage(sender, "server.not_found", true, false);
            return null;
        }

        return server;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        List<String> kits = new ArrayList<>(ServerHandler.getServerNames());
        Parser.pruneSuggestions(kits, lastWord);
        return kits;
    }
}
