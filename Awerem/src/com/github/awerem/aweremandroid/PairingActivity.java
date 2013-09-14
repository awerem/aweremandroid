package com.github.awerem.aweremandroid;

import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class PairingActivity extends Activity
{

    private Timer discoveryTaskTimer;
    private ArrayAdapter<String> computerAdapter = null;
    private ListView computerList;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);
        computerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1);
        computerList = (ListView) findViewById(R.id.available_computers);
        computerList.setAdapter(computerAdapter);
        spinner = (ProgressBar) findViewById(R.id.pairing_spinner);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        discoveryTaskTimer = new Timer();
        discoveryTaskTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run()
            {
                Log.i("PairingActivity", "Discovery task triggered");
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

    public void addToComputersList(InetAddress value)
    {
        if(computerAdapter.getPosition(value.getHostAddress()) == -1)
        {
            computerAdapter.add(value.getHostAddress());
        }
        if (!computerAdapter.isEmpty() && spinner.getVisibility() != View.GONE)
        {
            spinner.setVisibility(View.GONE);
            computerList.setVisibility(View.VISIBLE);
        }
    }

}
