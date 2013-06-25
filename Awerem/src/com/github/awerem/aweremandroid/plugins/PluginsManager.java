package com.github.awerem.aweremandroid.plugins;

import java.util.ArrayList;

import android.util.Log;

public class PluginsManager
{
    private String url = null;
    private ArrayList<PluginInfo> plugins = null;
    private onPluginsInfoLoadedListener callback = null;
    private PluginInfo active = null;

    public PluginsManager(onPluginsInfoLoadedListener callback, String ip)
    {
    	this.url = "http://" + ip + ":34340/core?get=plugin_list";
        this.callback = callback;
    }

    public String getActivePluginName()
    {
        if (active != null)
        {
            Log.d("PluginsManager", active.getName());
            return active.getName();
        }
        else
            return null;
    }

    public void gatherPlugins()
    {
        new getPluginsInfoAsyncTask(this).execute(url);

    }

    public void onPluginInfoReceived()
    {
        boolean changeActive = false;
        if (active == null)
        {
            active = plugins.get(0);
            changeActive = true;
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
