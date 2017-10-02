package com.desiremc.hub.session;

import java.util.List;

import org.bukkit.Bukkit;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.hub.DesireHub;

public class ServerHandler extends BasicDAO<Server, Long>
{

    private static ServerHandler instance;

    private List<Server> servers;

    public ServerHandler()
    {
        super(Server.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        servers = find().asList();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(DesireHub.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                update();
            }
        }, 0, 200);
    }

    public Server getServer(String server)
    {
        for (Server s : servers)
        {
            if (s.getName().equalsIgnoreCase(server))
            {
                return s;
            }
        }
        return null;
    }

    public void update()
    {
        servers = find().asList();
    }

    public List<Server> getServers()
    {
        return servers;
    }

    public static void initialize()
    {
        instance = new ServerHandler();
    }

    public static ServerHandler getInstance()
    {
        return instance;
    }

}
