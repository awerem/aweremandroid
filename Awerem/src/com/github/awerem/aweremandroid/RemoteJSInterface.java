package com.github.awerem.aweremandroid;

import java.net.MalformedURLException;
import java.net.URL;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class RemoteJSInterface
{
    private String moduleName = null;
    private PollManager pollmanager = null;
    private XMLRPCClient proxy = null;

    public RemoteJSInterface(String moduleName, String mIp, PollManager pollmanager)
    {
        this.moduleName = moduleName;
        this.pollmanager = pollmanager;
        URL url = null;
        try
        {
            url = new URL("http", mIp, 34340,
                    "action");
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
    public Object sendAction(String method, Object... objects)
    {
    	Log.i("JSInterface", "action sent");
        method = moduleName + "." + method;
        Object ret = null;
        try
        {
            ret = this.proxy.callEx(method, objects);
        }
        catch (XMLRPCException e)
        {
            e.printStackTrace();
        }
        return ret;
    }

    @JavascriptInterface
    public String getInfo()
    {
        return pollmanager.getInfoAsJsonString(moduleName);
    }
}
