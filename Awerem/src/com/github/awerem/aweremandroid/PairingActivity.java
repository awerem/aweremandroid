package com.github.awerem.aweremandroid;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class PairingActivity extends Activity
{
    private Timer discoveryTaskTimer;
    private ArrayAdapter<ComputerData> computerAdapter = null;
    private ListView computerList;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);
        computerAdapter = new ArrayAdapter<ComputerData>(this,
                android.R.layout.simple_list_item_activated_1);
        computerList = (ListView) findViewById(R.id.available_computers);
        computerList.setAdapter(computerAdapter);
        computerList
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                            View view, int position, long id)
                    {
                        ComputerData comp = (ComputerData) adapterView.getItemAtPosition(position);
                        Intent openRemote = new Intent(PairingActivity.this,
                                RemoteActivity.class);
                        openRemote.putExtra("ip", comp.ip);
                        startActivity(openRemote);
                        finish();
                    }
                });
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

    public void addToComputersList(ComputerData value)
    {
        boolean add = true;
        for(int i = 0; i < computerAdapter.getCount(); i++)
        {
            if(computerAdapter.getItem(i).equals(value))
                add = false;
        }
        if (add)
        {
            computerAdapter.add(value);
        }
        if (!computerAdapter.isEmpty() && spinner.getVisibility() != View.GONE)
        {
            spinner.setVisibility(View.GONE);
            computerList.setVisibility(View.VISIBLE);
        }
    }

}
