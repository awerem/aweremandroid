package com.github.awerem.aweremandroid;

import java.net.InetAddress;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import com.github.awerem.aweremandroid.utils.Utils;

public class PairingActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);
        new AsyncTask<Object, InetAddress, ArrayList<InetAddress>>() {

            @Override
            protected ArrayList<InetAddress> doInBackground(Object... params)
            {
                return Utils.getServersIp();
            }
            
            @Override
            protected void onProgressUpdate(InetAddress... values)
            {
                super.onProgressUpdate(values);
                if(values.length > 0)
                {
                    Intent openRemote = new Intent(PairingActivity.this, RemoteActivity.class);
                    openRemote.putExtra("ip", values[0].getHostAddress());
                    startActivity(openRemote);
                    cancel(true);
                }
            }
            
            @Override
            protected void onCancelled()
            {
                super.onCancelled();
                finish();
            }
            
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pairing, menu);
        return true;
    }

}
