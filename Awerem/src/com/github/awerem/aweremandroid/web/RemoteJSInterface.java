package com.github.awerem.aweremandroid.web;

import java.io.IOException;
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
    public String sendAction(int id, String method)
    {
        Log.i("JSInterface", "action sent " + method + " with id: " + id);
        method = moduleName + "." + method;
        JSReturner jsreturner = new JSReturner();
        String str_ret = "";
        Object ret = null;
        try
        {
            if (args.get(id) != null)
            {
                Object[] arguments = args.get(id).toArray();
                ret = this.proxy.callEx(method, arguments);
            }
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
        if (ret != null)
        {
            Log.i("JSInterface", "return value for id "+ id + ": " + ret.toString());
        }
        try
        {
            jsreturner.addObject(ret);
            str_ret = jsreturner.toJson();
            jsreturner.close();
        }
        catch (IOException e)
        {
        }
        return str_ret;
    }

    @JavascriptInterface
    public void sendActionAsync(int id, String method)
    {
        new AsyncTask<Object, Void, Object>() {
            int id = -1;

            @Override
            protected Object doInBackground(Object... params)
            {
                id = (Integer) params[0];
                String method = (String) params[1];
                Log.d("JSInterface", "\"" + method + "\" " + "\"" + id + "\"");
                Object result = RemoteJSInterface.this.sendAction(id, method);
                return result;
            }

            @Override
            protected void onPostExecute(Object result)
            {
                RemoteJSInterface.this.triggerCallback(id, result);
                super.onPostExecute(result);
            }
        }.execute(id, method);
    }

    protected void triggerCallback(int id, Object result)
    {
        Log.i("JSInterface", "triggerCallback with id: " + id);
        if (result != null)
        {
            String str_result = String.valueOf(result);
            webview.loadUrl("javascript:awerem.triggerCallback(" + id + ","
                    + str_result + ");");
        }
        else
        {
            webview.loadUrl("javascript:awerem.triggerCallback(" + id + ");");
        }
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

    @JavascriptInterface
    public void addString(int id, String obj)
    {
        Log.d("JSInterface", obj.toString());
        if (args.get(id) == null)
            args.append(id, new ArrayList<Object>());
        args.get(id).add(obj);
    }

    @JavascriptInterface
    public void addInt(int id, long obj)
    {
        Log.d("JSInterface", String.valueOf(obj));
        if (args.get(id) == null)
            args.append(id, new ArrayList<Object>());
        args.get(id).add(obj);
    }

    @JavascriptInterface
    public void addBool(int id, boolean obj)
    {
        Log.d("JSInterface", String.valueOf(obj));
        if (args.get(id) == null)
            args.append(id, new ArrayList<Object>());
        args.get(id).add(obj);
    }

    @JavascriptInterface
    public void addFloat(int id, double obj)
    {
        Log.d("JSInterface", String.valueOf(obj));
        if (args.get(id) == null)
            args.append(id, new ArrayList<Object>());
        args.get(id).add(obj);
    }
}
