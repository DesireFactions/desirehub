package com.desiremc.hub.session;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.session.DeathBanHandler;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hub.DesireHub;
import com.desiremc.hub.gui.ServerGUI;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ServerHandler extends BasicDAO<Server, Long>
{

    private static ServerHandler instance;

    private static String serverSelector;
    private static String pearl;
    private static String info;
    private static String pvp;
    private static String hider;

    private static List<Server> servers;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private static MongoCollection<Document> collection;

    private ServerHandler()
    {
        super(Server.class, DesireCore.getInstance().getMongoWrapper().getDatastore());

        mongoClient = new MongoClient();
        database = mongoClient.getDatabase("hcf");
        collection = database.getCollection("hcf_sessions");

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
                server.setWhitelisted(search.getWhitelisted());
                server.update();
            }
        }
    }

    public static void processPlayer(Server server, Player player)
    {
        Session session = SessionHandler.getSession(player);
        DeathBan ban = DeathBanHandler.getDeathBan(session, server.getName());
        Document myDoc = collection.find(eq("uuid", session.getUniqueId())).first();

        if (ban != null)
        {
            if (myDoc != null && myDoc.getInteger("lives") > 0)
            {
                int lives = myDoc.getInteger("lives");
                collection.updateOne(eq("uuid", session.getUniqueId()), new Document("$set", new Document("lives", lives - 1)));
                ban.setRevived(true);
                ban.setReviver(session.getUniqueId());
                ban.setReviveReason("Used life.");
                ban.save();
                session.save();
            }
            else
            {
                DesireHub.getLangHandler().sendRenderMessage(player, "redirect.deathban", true, false, "{server}", server.getName(), "{time}", DateUtils.formatDateDiff(ban.getStartTime() + session.getRank().getDeathBanTime()).replaceAll(" ([0-9]{1,2}) (seconds|second)", ""));
                return;
            }
        }

        if ((server.getSlots() > server.getOnline() || session.getRank().canAutoLogin()) && server.getStatus() && (!server.getWhitelisted() || session.getRank().isStaff()))
        {
            sendToServer(server, player);
            clearQueues(session);
            return;
        }
        else if (!server.getQueue().contains(session))
        {
            clearQueues(session);
            List<Session> queue = server.getQueue();

            if (session.getRank().isDonor() && !queue.isEmpty())
            {

                for (int i = 0; i < queue.size(); i++)
                {
                    Session s = queue.get(i);
                    if (session.getRank().getId() > s.getRank().getId())
                    {
                        queue.add(i, session);
                        DesireHub.getLangHandler().sendRenderMessage(session, "queue.location", true, false, "{server}", server.getName(), "{position}", String.valueOf(server.getQueueLocation(session)));
                        player.closeInventory();
                        return;
                    }
                }

                if (!queue.contains(session))
                {
                    server.addToQueue(session);
                }
            }
            else
            {
                server.addToQueue(session);
            }
        }
        DesireHub.getLangHandler().sendRenderMessage(session, "queue.location", true, false, "{server}", server.getName(), "{position}", String.valueOf(server.getQueueLocation(session)));
        player.closeInventory();
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

    public static List<String> getServerNames()
    {
        List<String> names = new ArrayList<>();
        for (Server s : servers)
        {
            names.add(s.getName());
        }
        return names;
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

    public static String getQueuePosition(Player player)
    {
        for (Server server : getServers())
        {
            if (server.getQueueLocation(player) != -1)
            {
                return server.getQueueLocation(player) + "";
            }
        }
        return "None";
    }

    public static ServerHandler getInstance()
    {
        return instance;
    }

}
