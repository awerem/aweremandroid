package com.github.awerem.aweremandroid.plugins;


public class PluginInfo implements Comparable<PluginInfo>
{
    private String name = null;
    private String title = null;
    private String category = null;
    private long priority = 999;
    
    public PluginInfo()
    {
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to define
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title the title to define
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return the category
     */
    public String getCategory()
    {
        return category;
    }

    /**
     * @param category the category to define
     */
    public void setCategory(String category)
    {
        this.category = category;
    }

    /**
     * @return le priority
     */
    public long getPriority()
    {
        return priority;
    }

    /**
     * @param priority le priority à définir
     */
    public void setPriority(long priority)
    {
        this.priority = priority;
    }


    
    @Override
    public String toString()
    {
        return "name: " + name + "; title: " + title + "; category: " + category;
    }

    @Override
    public int compareTo(PluginInfo another)
    {
        if (this.category == another.getCategory())
        {
            if (this.priority < another.getPriority())
                return -1;
            else if (this.priority > another.getPriority())
                return 1;
            else
                return 0;
        }
        else if (this.category == "contextual")
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }
}