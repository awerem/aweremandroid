package com.github.awerem.aweremandroid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
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
    private HashMap<String, JSONArray> data;
    private WeakReference<RemoteActivity> activity;
    private final Object lock = new Object();

    public PollManager(RemoteActivity remoteactivity, final String host)
    {
        data = new HashMap<String, JSONArray>();
        activity = new WeakReference<RemoteActivity>(remoteactivity);
        Thread thread = new Thread(new Runnable() {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        URL url = new URL(host);
                        URLConnection connection = url.openConnection();
                        connection.setUseCaches(true);
                        InputStream is = (InputStream) connection.getContent();
                        String jsonString = new Scanner(is, "UTF-8")
                                .useDelimiter("\\A").next();
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
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }, "PollManagerThread");
        thread.start();
    }

    public String getInfoAsJsonString(String moduleName)
    {
        synchronized (lock)
        {

        }
        return null;
    }

}
