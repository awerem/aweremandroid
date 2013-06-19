package com.github.awerem.aweremandroid;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONStringer;
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

    public RemoteJSInterface(String moduleName, WebView view, PollManager pollmanager)
    {
        this.moduleName = moduleName;
        this.pollmanager = pollmanager;
        URL url_origin = null;
        URL url = null;
        try
        {
            url_origin = new URL(view.getOriginalUrl());
            url = new URL("http", url_origin.getHost(), url_origin.getPort(),
                    "actions");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        if (url != null)
        {
            Log.i("RemoteJSInterface", url.toString());
            this.proxy = new XMLRPCClient(url);
        }
    }

    @JavascriptInterface
    public void sendXMLRPC(String method, Object... objects)
    {
        method = moduleName + "." + method;
        try
        {
            this.proxy.callEx(method, objects);
        }
        catch (XMLRPCException e)
        {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public String getInfo()
    {
        return pollmanager.getInfoAsJsonString(moduleName);
    }
}
