package com.github.awerem.aweremandroid.internet;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Base64;
import android.util.Log;

public class ServerDiscoverer
{
    private static final int AWEREM_PORT = 34340;
    private static final int TIMEOUT = 1000;
    private static final String DEBUG_TAG = "ServerDiscoverer";
    private WeakReference<Activity> mContext;

    public ServerDiscoverer(WeakReference<Activity> ctx)
    {
        mContext = ctx;
    }

    public InetAddress getBroadcastAddress() throws IOException
    {
        if (mContext.get() == null)
            return null;
        WifiManager wifi = (WifiManager) mContext.get().getSystemService(
                Context.WIFI_SERVICE);
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
        MulticastSocket socket = null;
        byte[] buf = new byte[128];
        DatagramPacket answer = new DatagramPacket(buf, buf.length);
        ArrayList<InetAddress> addresses = new ArrayList<InetAddress>();
        try
        {
            socket = new MulticastSocket(AWEREM_PORT);
            socket.setBroadcast(true);
            socket.setSoTimeout(TIMEOUT);
            byte[] rToken = new byte[128];
            SecureRandom.getInstance("SHA1PRNG").nextBytes(rToken);
            rToken = MessageDigest.getInstance("SHA1").digest(rToken);
            String token = Base64.encodeToString(rToken, Base64.DEFAULT);
            String requete = "awerem\nping\n" + token+"\n";
            DatagramPacket packet = new DatagramPacket(requete.getBytes(), 
                    requete.length(), getBroadcastAddress(), AWEREM_PORT);
            socket.send(packet);

            try
            {  
                while(true)
                {
                    socket.receive(answer);
                    Log.d("SOCKET", "Socket received");
                    Log.d("SOCKET", new String(answer.getData(), "ascii"));
                    String content = new String(answer.getData(), "ascii");
                    String[] lines = content.split("\\r?\\n");
                    if(lines[1].startsWith("pong") && lines[2].equals(token))
                    {
                        addresses.add(answer.getAddress());
                    }
                }
            }
            catch(SocketTimeoutException e)
            {
                Log.i(DEBUG_TAG, "socket closed");
                socket.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return addresses;
    }
}
