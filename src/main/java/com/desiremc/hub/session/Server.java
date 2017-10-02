package com.desiremc.hub.session;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "servers", noClassnameStored = true)
public class Server
{

    @Id
    private int id;
    
    private String name;

    private int maxCount;

    private int currentCount;

    public Server(String name, int maxCount, int currentCount)
    {
        this.name = name;
        this.maxCount = maxCount;
        this.currentCount = currentCount;
    }

    public Server()
    {
    }

    public String getName()
    {
        return name;
    }

    public int getMaxCount()
    {
        return maxCount;
    }

    public int getCurrentCount()
    {
        return currentCount;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setMaxCount(int maxCount)
    {
        this.maxCount = maxCount;
    }

    public void setCurrentCount(int currentCount)
    {
        this.currentCount = currentCount;
    }

}
