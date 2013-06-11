package com.github.awerem.aweremandroid;

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
        
        private Activity ctx;

        public AsyncGetServers(Activity ctx)
        {
            this.ctx = ctx;
        }
        
        @Override
        protected ArrayList<InetAddress> doInBackground(Void... params)
        {
            final ServerDiscoverer discoverer = new ServerDiscoverer(ctx);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run()
                {
                    for(InetAddress address : discoverer.gatherIPs())
                        publishProgress(address);
                }
            }, 0, 1000);
            return discoverer.AllIPs();
        }

        @Override
        protected void onProgressUpdate(InetAddress... values)
        {
            super.onProgressUpdate(values);
            if (values.length > 0)
            {
                Intent openRemote = new Intent(ctx,
                        RemoteActivity.class);
                openRemote.putExtra("ip", values[0].getHostAddress());
                ctx.startActivity(openRemote);
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