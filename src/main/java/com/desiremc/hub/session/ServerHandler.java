package com.desiremc.hub.session;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.session.DeathBanHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.gui.ServerGUI;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerHandler extends BasicDAO<Server, Long>
{

    private static ServerHandler instance;

    private static String serverSelector;
    private static String pearl;
    private static String info;
    private static String pvp;
    private static String hider;

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
        }, 20, 100);
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
        DeathBan ban = DeathBanHandler.getDeathBan(s, server.getName());
        if (ban != null)
        {
            DesireHub.getLangHandler().sendRenderMessage(player, "redirect.deathban", true, false, "{server}", server.getName(), "{time}", DateUtils.formatDateDiff(ban.getStartTime() + s.getRank().getDeathBanTime()).replaceAll(" ([0-9]{1,2}) (seconds|second)", ""));
            return;
        }

        if ((server.getSlots() > server.getOnline() || s.getRank().isStaff() || s.getRank() == Rank.GRANDMASTER || s.getRank() == Rank.BETA) && server.getStatus())
        {
            sendToServer(server, player);
            clearQueues(s);
            return;
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
                    if (queue.hasNext() && next.getRank().getId() < s.getRank().getId())
                    {
                        queue.add(next);
                        queue.set(s);
                        DesireHub.getLangHandler().sendRenderMessage(next, "queue.location", true, false, "{server}", server.getName(), "{position}", String.valueOf(server.getQueueLocation(next)));
                    }
                    else
                    {
                        queue.add(next);
                    }
                }
            }
            else
            {
                server.addToQueue(s);
            }
        }
        DesireHub.getLangHandler().sendRenderMessage(s, "queue.location", true, false, "{server}", server.getName(), "{position}", String.valueOf(server.getQueueLocation(s)));
        player.closeInventory();
    }

    public static void sendToServer(Server server, Player player)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server.getName());
        player.sendPluginMessage(DesireHub.getInstance(), "BungeeCord", out.toByteArray());

        for (Player target : Bukkit.getOnlinePlayers())
        {
            if (server.getQueueLocation(target) == -1)
            {
                continue;
            }

            DesireHub.getLangHandler().sendRenderMessage(target, "queue.location", true, false, "{server}", server.getName(), "{position}", String.valueOf(server.getQueueLocation(target)));
        }
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

    public static int getAllPlayers()
    {
        int count = 0;

        for (Server server : getServers())
        {
            count += server.getOnline();
        }
        return count;
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

    public static String getPearl()
    {
        if (pearl == null)
        {
            pearl = DesireHub.getConfigHandler().getString("pearl.name");
        }
        return pearl;
    }

    public static String getInfo()
    {
        if (info == null)
        {
            info = DesireHub.getConfigHandler().getString("info.name");
        }
        return info;
    }

    public static String getPvP()
    {
        if (pvp == null)
        {
            pvp = DesireHub.getConfigHandler().getString("pvp.name");
        }
        return pvp;
    }

    public static String getHider()
    {
        if (hider == null)
        {
            hider = DesireHub.getConfigHandler().getString("hider.name");
        }
        return hider;
    }

    public static ServerHandler getInstance()
    {
        return instance;
    }

}
