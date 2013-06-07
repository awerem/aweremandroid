package com.github.awerem.aweremandroid.plugins;

import java.util.ArrayList;

import android.util.Log;

public class PluginsManager
{
    private static String URL = "http://192.168.1.14:34340/core?get=plugin_list";
    private ArrayList<PluginInfo> plugins = null;
    private onPluginsInfoLoadedListener callback = null;
    private PluginInfo active = null;

    public PluginsManager(onPluginsInfoLoadedListener callback)
    {
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
        new getPluginsInfoAsyncTask(this).execute(URL);

    }
    
    public void onPluginInfoReceived()
    {
        active = plugins.get(1);
        if(callback != null)
        {
            callback.onPluginsInfoLoaded();
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

}
