package com.github.awerem.aweremandroid.plugins;

import java.util.ArrayList;
import java.util.Collections;

public class PluginsManager
{
    private String url = null;
    private ArrayList<PluginInfo> plugins = null;
    private onPluginsInfoLoadedListener callback = null;
    private PluginInfo active = null;
    private String nextActiveName;

    public PluginsManager(String ip)
    {
    	this.url = "http://" + ip + ":34340/core?get=plugin_list";
    }

    public String getActivePluginName()
    {
        if (active != null)
        {
            return active.getName();
        }
        else
            return null;
    }

    public void gatherPlugins(onPluginsInfoLoadedListener callback, String activeName)
    {
        new getPluginsInfoAsyncTask(this).execute(url);
        nextActiveName = activeName;
        this.callback = callback;
    }
    
    public void onPluginInfoReceived()
    {
        boolean changeActive = false;
        if (plugins != null)
        {
            Collections.sort(plugins);
            if (active == null && plugins.size() > 0)
            {
                if(nextActiveName != null)
                    setActive(nextActiveName);
                if(active == null)
                {
                    active = plugins.get(0);
                }
                changeActive = true;
            }
        }
        if (callback != null)
        {
            callback.onPluginsInfoLoaded(true, changeActive);
        }
    }

    public void setPlugins(ArrayList<PluginInfo> result)
    {
        this.plugins = result;
    }

    public ArrayList<PluginInfo> getPlugins()
    {
        return plugins;
    }

    public String getActivePluginTitle()
    {
        if (active == null)
            return "AweRem";
        else
            return active.getTitle();
    }

    public void setActive(String name)
    {
        for(PluginInfo plugin : plugins)
        {
            if(plugin.getName().equals(name))
            {
                active = plugin;
                break;
            }
        }
    }
}
