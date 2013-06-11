package com.github.awerem.aweremandroid.utils;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;

import com.github.awerem.aweremandroid.R;
import com.github.awerem.aweremandroid.navigation.Header;
import com.github.awerem.aweremandroid.navigation.Item;
import com.github.awerem.aweremandroid.navigation.RemoteItem;
import com.github.awerem.aweremandroid.plugins.PluginInfo;

public class Utils
{

    public static ArrayList<Item> createNavList(ArrayList<PluginInfo> plugins,
            Context ctx)
    {
        ArrayList<Item> list = new ArrayList<Item>();
        String curCat = "";
        Collections.sort(plugins);
        for (PluginInfo plugin : plugins)
        {
            if (!curCat.equals(plugin.getCategory()))
            {
                curCat = plugin.getCategory();
                if (curCat.equals("contextual"))
                    list.add(new Header(ctx.getResources().getString(
                            R.string.contextual)));
                else if (curCat.equals("utils"))
                    list.add(new Header(ctx.getResources().getString(
                            R.string.utils)));
            }
            list.add(new RemoteItem(plugin));
        }

        return list;
    }

}
