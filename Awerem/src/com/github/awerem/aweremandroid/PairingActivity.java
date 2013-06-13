package com.github.awerem.aweremandroid;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class PairingActivity extends Activity
{

    private Timer discoveryTaskTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);
    }

    @Override
    protected void onResume()
    {
        super.onStart();
        discoveryTaskTimer = new Timer();
        discoveryTaskTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run()
            {
                Log.i("PairingActivity" ,"Discovery task triggered");
                AsyncGetServers async = new AsyncGetServers(
                        PairingActivity.this);
                async.execute();
            }
        }, 0, 3000);
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        discoveryTaskTimer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pairing, menu);
        return true;
    }

}
