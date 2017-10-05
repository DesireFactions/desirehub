package com.desiremc.hub.session;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "servers", noClassnameStored = true)
public class Server
{

    @Id
    private int id;
    
    private String name;

    private int online;

    private int slots;

    public Server(String name, int slots, int online)
    {
        this.name = name;
        this.slots = slots;
        this.online = online;
    }

    public Server()
    {
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

}
