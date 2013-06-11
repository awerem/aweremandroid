package com.github.awerem.aweremandroid;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.github.awerem.aweremandroid.internet.ServerDiscoverer;

class AsyncGetServers extends
        AsyncTask<Void, InetAddress, ArrayList<InetAddress>>
{

    private WeakReference<Activity> ctx;

    public AsyncGetServers(Activity ctx)
    {
        this.ctx = new WeakReference<Activity>(ctx);
    }

    @Override
    protected ArrayList<InetAddress> doInBackground(Void... params)
    {
        ServerDiscoverer discoverer = new ServerDiscoverer(ctx);
        return discoverer.gatherIPs();

    }

    @Override
    protected void onPostExecute(ArrayList<InetAddress> result)
    {
        if (ctx.get() != null && !result.isEmpty())
        {
            Intent openRemote = new Intent(ctx.get(), RemoteActivity.class);
            openRemote.putExtra("ip", result.get(0).getHostAddress());
            ctx.get().startActivity(openRemote);
            ctx.get().finish();
        }
        super.onPostExecute(result);
    }
}