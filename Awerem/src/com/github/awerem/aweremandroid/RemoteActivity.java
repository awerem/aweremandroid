package com.github.awerem.aweremandroid;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

public class RemoteActivity extends Activity
{

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        WebView remoteView = (WebView) findViewById(R.id.remote_view);
        remoteView.loadUrl("http://192.168.1.14:34340/ui/redbutton");
        remoteView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.remote, menu);
        return true;
    }
}
