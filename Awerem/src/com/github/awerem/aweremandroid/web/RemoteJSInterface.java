package com.github.awerem.aweremandroid.web;

import java.net.MalformedURLException;
import java.net.URL;

import org.xmlrpc.android.XMLRPCClient;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.github.awerem.aweremandroid.PollManager;

public class RemoteJSInterface
{
    private String moduleName = null;
    private PollManager pollmanager = null;
    private XMLRPCClient proxy = null;
    private String tickName;
    private long tickStart;
    private WebView webview;

    public RemoteJSInterface(String moduleName, String mIp,
            PollManager pollmanager, WebView webview)
    {
        this.moduleName = moduleName;
        this.pollmanager = pollmanager;
        this.webview = webview;
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
        Log.d("JSInterface", "");
        return ret;
    }

    @JavascriptInterface
    public void sendActionAsync(String method, String json, String callback)
    {
        new AsyncTask<String, Void, Pair<String, String>>() {

            @Override
            protected Pair<String, String> doInBackground(String... params)
            {
                Log.d("JSInterface", "\"" + params[0] + "\" " + "\""
                        + params[1] + "\"");
                String result = RemoteJSInterface.this.sendAction(params[0],
                        params[1]);
                if (result == null)
                {
                	result = "";
                }
                return new Pair<String, String>(params[2], result);
            }

            @Override
            protected void onPostExecute(Pair<String, String> result)
            {
                if(result.first != null && result.first != "")
                {
                    String injection = result.first + "('"
                            + result.second.replace("'", "\\'") + "');";
                    RemoteJSInterface.this.injectJS(injection);
                }
                super.onPostExecute(result);
            }
        }.execute(method, json, callback);
    }

    protected void injectJS(String injection)
    {
        webview.loadUrl("javascript:" + injection);
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
