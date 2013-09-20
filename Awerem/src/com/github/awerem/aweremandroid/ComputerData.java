package com.github.awerem.aweremandroid;

public class ComputerData
{
    public String name = null;
    public String ip = null;
    public String uuid;
    public int seq = 0;

    public ComputerData(String name, String ip, String uuid)
    {
        this.name = name;
        this.ip = ip;
        this.uuid = uuid;
    }
    
    @Override
    public String toString()
    {
        return this.name;
    }
    
    @Override
    public boolean equals(Object o)
    {
        ComputerData other = null;
        if (this == o) return true;
        if(o instanceof ComputerData)
            other = (ComputerData) o;
            if(other.ip.equals(this.ip) && other.name.equals(this.name)
                    && other.uuid.equals(this.uuid))
                return true;
        return false;
    }
}