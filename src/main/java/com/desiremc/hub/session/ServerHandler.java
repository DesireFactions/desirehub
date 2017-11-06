package com.desiremc.hub.session;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.google.common.io.ByteArrayDataOutput;
import com.desiremc.google.common.io.ByteStreams;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.gui.ServerGUI;

public class ServerHandler extends BasicDAO<Server, Long>
{

    private static ServerHandler instance;

    private static String serverSelector;

    private static List<Server> servers;

    private ServerHandler()
    {
        super(Server.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        DesireCore.getInstance().getMongoWrapper().getMorphia().map(Server.class);

        servers = find().asList();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(DesireHub.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                update();
                ServerGUI.getInstance().reloadServers();
            }
        }, 0, 100);
    }

    public static void update()
    {
        Server search;
        for (Server server : servers)
        {
            search = instance.findOne("id", server.getId());
            if (search != null)
            {
                server.setOnline(search.getOnline());
                server.setMaxCount(search.getSlots());
                server.update();
            }
        }
    }

    public static void processPlayer(Server server, Player player)
    {
        Session s = SessionHandler.getSession(player);
        HCFSession hs = HCFSessionHandler.initializeHCFSession(player.getUniqueId(), server.getName(), false);
        DeathBan ban = hs.getActiveDeathBan();
        if (hs.hasDeathBan())
        {
            DesireHub.getLangHandler().sendRenderMessage(player, "redirect.deathban", "{server}", server.getName(), "{time}", DateUtils.formatDateDiff(ban.getStartTime() + s.getRank().getDeathBanTime()).replaceAll(" ([0-9]{1,2}) (seconds|second)", ""));
            return;
        }

        if (server.getSlots() > server.getOnline() || s.getRank().isStaff() || s.getRank() == Rank.GRANDMASTER)
        {
            sendToServer(server, player);
            clearQueues(s);
        }
        else if (!server.getQueue().contains(s))
        {
            clearQueues(s);
            ListIterator<Session> queue = server.getQueue().listIterator();
            if (s.getRank().isDonor() && queue.hasNext())
            {
                Session next;
                while (queue.hasNext())
                {
                    next = queue.next();
                    if (next.getRank().getId() < s.getRank().getId() || !queue.hasNext())
                    {
                        queue.add(s);
                    }
                }
            }
            else
            {
                server.addToQueue(s);
            }
        }
        else
        {
            DesireHub.getLangHandler().sendRenderMessage(s, "queue.location", "{server}", server.getName(), "{position}", String.valueOf(server.getQueueLocation(s)));
        }
    }

    public static void sendToServer(Server server, Player player)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server.getName());
        player.sendPluginMessage(DesireHub.getInstance(), "BungeeCord", out.toByteArray());
    }

    private static void clearQueues(Session s)
    {
        for (Server server : servers)
        {
            if (server.isInQueue(s))
            {
                server.removeFromQueue(s);
            }
        }
    }

    public static Server getServer(String name)
    {
        for (Server s : servers)
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }

    public static Server getServer(int id)
    {
        for (Server s : servers)
        {
            if (s.getId() == id)
            {
                return s;
            }
        }
        return null;
    }

    public static List<Server> getServers()
    {
        return servers;
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
