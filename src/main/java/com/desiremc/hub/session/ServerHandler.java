package com.desiremc.hub.session;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.gui.ServerGUI;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerHandler extends BasicDAO<Server, Long>
{

    private static ServerHandler instance;

    private static String serverSelector;

    private static List<Server> servers;

    private static HashMap<Integer, List<Player>> queue;

    private ServerHandler()
    {
        super(Server.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        servers = find().asList();

        queue = new HashMap<>();
        for (Server server : servers)
        {
            queue.put(server.getId(), new LinkedList<>());
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(DesireHub.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                update();
                ServerGUI.loadServers();
            }
        }, 0, 200);
    }

    public static Server getServer(String server)
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

    public static void update()
    {
        servers = instance.find().asList();
    }

    public static void processPlayer(Server server, Player player)
    {
        if (server.getSlots() > server.getOnline())
        {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(server.getName());
            player.sendPluginMessage(DesireHub.getInstance(), "BungeeCord", out.toByteArray());
        }
    }

    public static List<Server> getServers()
    {
        return servers;
    }

    public static void addQueue(int server, Player p)
    {
        List<Player> players = queue.get(server);
        if (players != null)
        {
            players.add(p);
        }
    }

    public static int getQueueCount(int server)
    {
        return queue.get(server).size();
    }

    public static void initialize()
    {
        instance = new ServerHandler();
    }

    public static String getServerSelector()
    {
        if (serverSelector == null)
        {
            serverSelector = DesireHub.getConfigHandler().getString("selector.name");
        }
        return serverSelector;
    }

    public static ServerHandler getInstance()
    {
        return instance;
    }

}
