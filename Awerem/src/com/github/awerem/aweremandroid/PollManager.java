package com.github.awerem.aweremandroid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.github.awerem.aweremandroid.utils.Utils;

public class PollManager
{
    private final class PollManagerThread implements Runnable
    {
        private final String host;

        private PollManagerThread(String host)
        {
            this.host = host;
        }

        public void run()
        {
            while (true)
            {
                try
                {
                    URL url = new URL(host);
                    URLConnection connection = url.openConnection();
                    connection.setReadTimeout(0);
                    connection.setUseCaches(true);
                    InputStream is = (InputStream) connection.getContent();
                    String jsonString = new Scanner(is, "UTF-8").useDelimiter(
                            "\\A").next();
                    is.close();
                    JSONObject json = new JSONObject(jsonString);
                    Iterator<?> iter = json.keys();
                    synchronized (lock)
                    {
                        while (iter.hasNext())
                        {
                            String key = (String) iter.next();
                            JSONArray tmp = json.getJSONArray(key);
                            if (data.containsKey(key))
                            {
                                data.put(key, Utils.concatJSONArray(
                                        data.get(key), tmp));
                            }
                            else
                            {
                                data.put(key, tmp);
                            }
                        }
                    }
                }
                catch (FileNotFoundException e)
                {
                    Log.w("PollManager", "file not found at " + host);
                }
                catch (SocketTimeoutException e)
                {
                    Log.w("PollManager",
                            "The PollManager should never time out");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                PollManager.this.remoteactivity.get().runOnUiThread(
                        new Runnable() {

                            @Override
                            public void run()
                            {
                                updateNavDrawerIfRequired();
                            }
                        });
            }
        }
    }

    private HashMap<String, JSONArray> data;
    private final Object lock = new Object();
    private WeakReference<RemoteActivity> remoteactivity;

    public PollManager(RemoteActivity remoteactivity, final String host)
    {
        this.remoteactivity = new WeakReference<RemoteActivity>(remoteactivity);
        data = new HashMap<String, JSONArray>();
        Thread thread = new Thread(new PollManagerThread(host),
                "PollManagerThread");
        thread.start();
    }

    public String getInfoAsJsonString(String moduleName)
    {
        String json_string = "";
        synchronized (lock)
        {
            if (data.containsKey(moduleName))
            {
                json_string = data.get(moduleName).toString();
                data.remove(moduleName);
            }
        }
        return json_string;
    }

    public void updateNavDrawerIfRequired()
    {
        boolean updateRequired = false;
        JSONArray newArray = new JSONArray();
        synchronized (lock)
        {
            if (data.containsKey("core"))
            {
                for (int i = 0; i < data.get("core").length(); i++)
                {
                    if (data.get("core").optString(i)
                            .equals("UpdateNavigationDrawer"))
                    {
                        updateRequired = true;
                    }
                    else
                    {
                        newArray.put(data.get("core").opt(i));
                    }
                }
            }
            data.put("core", newArray);
        }
        if (updateRequired)
        {
            remoteactivity.get().triggerPluginGathering();
        }
    }

}
