package com.github.awerem.aweremandroid.navigation;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.awerem.aweremandroid.R;
import com.github.awerem.aweremandroid.navigation.NavigationArrayAdapter.RowType;
import com.github.awerem.aweremandroid.plugins.PluginInfo;
import com.github.awerem.aweremandroid.utils.DownloadImageTask;

public class RemoteItem implements Item, Comparable<RemoteItem>
{
    private PluginInfo plugin;
    private String ip;

    public RemoteItem(PluginInfo plugin, String ip)
    {
        this.setPlugin(plugin);
        this.ip = ip;
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
        TextView titleview = (TextView) view.findViewById(R.id.navremote);
        titleview.setText(getPlugin().getTitle());
        String dpi = "";
        switch (view.getResources().getDisplayMetrics().densityDpi)
        {
        case (DisplayMetrics.DENSITY_LOW):
            dpi = "ldpi";
            break;
        case (DisplayMetrics.DENSITY_HIGH):
            dpi = "hdpi";
            break;
        case (DisplayMetrics.DENSITY_XHIGH):
            dpi = "xhdpi";
            break;
        case (DisplayMetrics.DENSITY_XXHIGH):
            dpi = "xxhdpi";
            break;
        default:
            dpi = "mdpi";
            break;

        }
        new DownloadImageTask((ImageView)
        view.findViewById(R.id.navremoteicon))
        .execute("http://" + ip + ":34340/ui/"+getPlugin().getName() +
        "/?get=icon&dpi="+dpi);
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
