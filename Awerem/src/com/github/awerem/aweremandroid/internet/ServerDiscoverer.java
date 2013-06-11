package com.github.awerem.aweremandroid.internet;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

public class ServerDiscoverer
{
    private WeakReference<Activity> mContext;

    public ServerDiscoverer(WeakReference<Activity> ctx)
    {
        mContext = ctx;
    }
    
    InetAddress getBroadcastAddress() throws IOException {
        if(mContext.get() == null)
            return null;
        WifiManager wifi = (WifiManager) mContext.get().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
          quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    public ArrayList<InetAddress> gatherIPs()
    {
        return null;
    }

    public ArrayList<InetAddress> AllIPs()
    {
        return null;
    }
}