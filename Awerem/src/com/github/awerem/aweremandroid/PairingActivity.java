package com.github.awerem.aweremandroid;

import java.net.InetAddress;
import java.util.ArrayList;

import android.app.Activity;
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
        new AsyncTask<Object, Boolean, ArrayList<InetAddress>>() {

            @Override
            protected Boolean doInBackground(Object... params)
            {
                Utils.getServersIp
                return true;
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
