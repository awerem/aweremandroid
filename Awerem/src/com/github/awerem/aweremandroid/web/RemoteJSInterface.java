package com.github.awerem.aweremandroid.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlrpc.android.XMLRPCClient;

import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
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
    private SparseArray<ArrayList<Object>> args;

    public RemoteJSInterface(String moduleName, String mIp,
            PollManager pollmanager, WebView webview)
    {
        this.moduleName = moduleName;
        this.pollmanager = pollmanager;
        this.webview = webview;
        this.args = new SparseArray<ArrayList<Object>>();
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
    public Object sendAction(int id, String method)
    {
  		Log.i("JSInterface", "action sent " + method + " with id: " + id);
        method = moduleName + "." + method;
        Object ret = null;
        try
        {
        	if(args.get(id) != null)
        		ret = this.proxy.call(method, args.get(id).toArray());
        	else
        		ret = this.proxy.call(method);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
        	args.delete(id);
        }
        return ret;
    }

    @JavascriptInterface
    public void sendActionAsync(int id, String method)
    {
        new AsyncTask<Object, Void, String>() {

            @Override
            protected String doInBackground(Object... params)
            {
            	int id = (Integer) params[0];
            	String method = (String) params[1];
                Log.d("JSInterface", "\"" + method + "\" " + "\""
                        + id + "\"");
                String result = String.valueOf(RemoteJSInterface.this.sendAction(id, method));
                if (result == null)
                {
                	result = "";
                }
                return result;
            }

            @Override
            protected void onPostExecute(final String result)
            {
               if(result != null)
                {
                    RemoteJSInterface.this.injectJS(result);
                }
                super.onPostExecute(result);
            }
        }.execute(id, method);
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
