package com.github.awerem.aweremandroid.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.awerem.aweremandroid.R;
import com.github.awerem.aweremandroid.navigation.NavigationArrayAdapter.RowType;
import com.github.awerem.aweremandroid.plugins.PluginInfo;

public class RemoteItem implements Item, Comparable<RemoteItem>
{
    private PluginInfo plugin;

    public RemoteItem(Context ctx, PluginInfo plugin)
    {
        this.setPlugin(plugin);
    }

    @Override
    public int getViewType()
    {
        return RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView)
    {
        View view;
        if (convertView == null)
        {
            view = (View) inflater.inflate(R.layout.drawer_list_remote, null);
        }
        else
        {
            view = convertView;
        }
        TextView titleview = (TextView) view.findViewById(R.id.remote_title);
        titleview.setText(getPlugin().getTitle());
        return view;
    }

    @Override
    public int compareTo(RemoteItem another)
    {
        return getPlugin().compareTo(another.getPlugin());
    }

    public PluginInfo getPlugin()
    {
        return plugin;
    }

    public void setPlugin(PluginInfo plugin)
    {
        this.plugin = plugin;
    }

}
