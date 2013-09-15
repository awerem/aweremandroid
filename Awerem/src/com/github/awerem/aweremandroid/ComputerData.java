package com.github.awerem.aweremandroid;

public class ComputerData
{
    public String name = null;
    public String ip = null;

    public ComputerData(String name, String ip)
    {
        this.name = name;
        this.ip = ip;
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
            if(other.ip.equals(this.ip) && other.name.equals(this.name))
                return true;
        return false;
    }
}