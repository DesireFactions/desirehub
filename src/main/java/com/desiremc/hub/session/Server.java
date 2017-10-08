package com.desiremc.hub.session;

import java.util.ListIterator;

import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.session.Session;
import com.desiremc.core.utils.LinkedList;

@Entity(value = "servers", noClassnameStored = true)
public class Server
{

    @Id
    private int id;

    private String name;

    private int online;

    private int slots;

    @Transient
    private LinkedList<Session> queue;

    public Server(String name, int slots, int online)
    {
        this.name = name;
        this.slots = slots;
        this.online = online;
        this.queue = new LinkedList<>();
    }

    public Server()
    {
        this.queue = new LinkedList<>();
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getSlots()
    {
        return slots;
    }

    public int getOnline()
    {
        return online;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setMaxCount(int slots)
    {
        this.slots = slots;
    }

    public void setOnline(int online)
    {
        this.online = online;
    }

    public void addToQueue(Session s)
    {
        queue.add(s);
    }

    public void removeFromQueue(Session s)
    {
        queue.remove(s);
    }

    public LinkedList<Session> getQueue()
    {
        return queue;
    }

    public void update(int online, int slots)
    {
        this.online = online;
        this.slots = slots;
        if (online < slots)
        {
            int forward = slots - online;
            ListIterator<Session> search = queue.listIterator();
            Session s;
            Player p;
            for (int i = 0; i < Math.min(forward, queue.size()); i++)
            {
                s = search.next();
                p = s.getPlayer();
                if (p != null)
                {
                    ServerHandler.sendToServer(this, p);
                }
                search.remove();
            }
        }
    }

}
