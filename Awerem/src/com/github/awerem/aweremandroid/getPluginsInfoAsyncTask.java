package com.github.awerem.aweremandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

class getPluginsInfoAsyncTask extends
        AsyncTask<String, Void, ArrayList<PluginInfo>>
{

    private static final String DEBUG_TAG = "getPluginAsync";
    private PluginsManager pm;

    public getPluginsInfoAsyncTask(PluginsManager pm)
    {
        super();
        this.pm = pm;
    }

    @Override
    protected ArrayList<PluginInfo> doInBackground(String... urls)
    {
        try
        {
            return makePluginsList(downloadUrl(urls[0]));
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private ArrayList<PluginInfo> makePluginsList(InputStream is) throws IOException
    {
        JsonReader reader = null;
        try
        {
        ArrayList<PluginInfo> pluginsList = new ArrayList<PluginInfo>();
        reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        reader.beginArray();
        while(reader.hasNext())
        {
            pluginsList.add(readPlugin(reader));
        }
        reader.endArray();
        return pluginsList;
        }
        finally
        {
            if(reader != null)
            {
                reader.close();
            }
            if(is != null)
                is.close();
        }
    }

    private PluginInfo readPlugin(JsonReader reader) throws IOException
    {
        PluginInfo plugin = new PluginInfo();
        reader.beginObject();
        while(reader.hasNext())
        {
            String name = reader.nextName();
            if(name.equals("name"))
            {
                plugin.setName(reader.nextString());
            }
            else if (name.equals("title"))
            {
                plugin.setTitle(reader.nextString());
            }
            else if (name.equals("category"))
            {
                plugin.setCategory(reader.nextString());
            }
            else if (name.equals("icon"))
            {
                if(reader.peek() == JsonToken.NULL)
                {
                    reader.skipValue();
                    plugin.setIcon(null);
                }
                else
                    plugin.setIcon(reader.nextString());
            }
            else if(name.equals("priority"))
            {
                plugin.setPriority(reader.nextLong());
            }
            else
            {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.i(DEBUG_TAG, plugin.toString());
        return plugin;
    }

    private InputStream downloadUrl(String myurl)
    {
        try
        {
        URL url = new URL(myurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        int response = conn.getResponseCode();
        Log.d(DEBUG_TAG, "The response is: " + response);
        return conn.getInputStream();
        }
        catch (Exception e)
        {
            Log.e(DEBUG_TAG, "weirdbug", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<PluginInfo> result)
    {
        pm.setPlugins(result);
        pm.onPluginInfoReceived();
    }
}