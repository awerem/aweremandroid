package com.github.awerem.aweremandroid.utils;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.github.awerem.aweremandroid.R;
import com.github.awerem.aweremandroid.navigation.Header;
import com.github.awerem.aweremandroid.navigation.Item;
import com.github.awerem.aweremandroid.navigation.RemoteItem;
import com.github.awerem.aweremandroid.plugins.PluginInfo;

public class Utils
{

    public static ArrayList<Item> createNavList(ArrayList<PluginInfo> plugins,
            Context ctx, String ip)
    {
        ArrayList<Item> list = new ArrayList<Item>();
        String curCat = "";
        Collections.sort(plugins);
        for (PluginInfo plugin : plugins)
        {
            if (!curCat.equals(plugin.getCategory()) && plugin.getPriority() >= 0)
            {
                curCat = plugin.getCategory();
                if (curCat.equals("contextual"))
                    list.add(new Header(ctx.getResources().getString(
                            R.string.contextual)));
                else if (curCat.equals("utils"))
                    list.add(new Header(ctx.getResources().getString(
                            R.string.utils)));
            }
            if (plugin.getPriority() >= 0)
            {
                list.add(new RemoteItem(plugin, ip));
            }
        }

        return list;
    }

    public static JSONArray concatJSONArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;

    }

}
