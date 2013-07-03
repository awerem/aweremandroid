package com.github.awerem.aweremandroid.web;

import java.net.MalformedURLException;
import java.net.URL;

import org.xmlrpc.android.XMLRPCClient;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.github.awerem.aweremandroid.PollManager;

public class RemoteJSInterface
{
    private String moduleName = null;
    private PollManager pollmanager = null;
    private XMLRPCClient proxy = null;
    private String tickName;
    private long tickStart;

    public RemoteJSInterface(String moduleName, String mIp,
            PollManager pollmanager)
    {
        this.moduleName = moduleName;
        this.pollmanager = pollmanager;
        URL url = null;
        try
        {
            url = new URL("http", mIp, 34340, "action");
        }
        catch (MalformedURLException e)
        {
            Log.w("RemoteJSInterface", "Malformed URL", e);
        }
        if (url != null)
        {
            Log.i("RemoteJSInterface", url.toString());
            this.proxy = new XMLRPCClient(url);
        }
    }

    @JavascriptInterface
    public String sendAction(String method, String json)
    {
        Log.i("JSInterface", "action sent " + method + " with args: " + json);
        method = moduleName + "." + method;
        String ret = null;
        try
        {
            ret = (String) this.proxy.call(method, json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d("JSInterface", ret);
        return ret;
    }

    @JavascriptInterface
    public String getInfo()
    {
        return pollmanager.getInfoAsJsonString(moduleName);
    }

    @JavascriptInterface
    public void tick(String name)
    {
        if (name != null)
        {
            this.tickName = name;
        }
        else
            this.tickName = "unnamed";
        this.tickStart = System.nanoTime();
    }

    @JavascriptInterface
    public void tack()
    {
        Log.i("RemoteJSInterface", "elapsed time for " + this.tickName + ": "
                + (System.nanoTime() - this.tickStart) / 1000000 + "ms");
    }
}
