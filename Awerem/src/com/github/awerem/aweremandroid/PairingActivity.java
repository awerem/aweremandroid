package com.github.awerem.aweremandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class PairingActivity extends Activity
{

   

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);
        AsyncGetServers async = new AsyncGetServers(this);
        async.execute();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pairing, menu);
        return true;
    }

}
