package com.github.awerem.aweremandroid;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
            return null;
            
        }

        @Override
        protected void onProgressUpdate(InetAddress... values)
        {
            super.onProgressUpdate(values);
            if (values.length > 0 && ctx.get() != null)
            {
                Intent openRemote = new Intent(ctx.get(),
                        RemoteActivity.class);
                openRemote.putExtra("ip", values[0].getHostAddress());
                ctx.get().startActivity(openRemote);
                cancel(true);
            }
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            ctx.finish();
        }
    }