package com.github.awerem.aweremandroid;

import com.github.awerem.aweremandroid.navigation.NavigationArrayAdapter;
import com.github.awerem.aweremandroid.plugins.PluginsManager;
import com.github.awerem.aweremandroid.plugins.onPluginsInfoLoadedListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemoteActivity extends Activity implements onPluginsInfoLoadedListener
{
    private String[] mRemotes = { "Redbutton", "System" };
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private WebView mRemoteView;
    private PluginsManager mPlugins;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        initDrawer();
        mRemoteView = (WebView) findViewById(R.id.remote_view);
        mRemoteView.getSettings().setJavaScriptEnabled(true);  
        mPlugins = new PluginsManager(this);
        mPlugins.gatherPlugins();
    }

    public void initDrawer()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mainLayout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the list's click listener
        // mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                getActionBar().setTitle(mPlugins.getActivePluginTitle());
                invalidateOptionsMenu(); // creates call to
                                         // onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                getActionBar().setTitle("Awerem");
                invalidateOptionsMenu(); // creates call to
                                         // onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // If the nav drawer is open, hide action items related to the content
        // view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.remote, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPluginsInfoLoaded()
    {
        //TODO Add a factory to create the array.
        //TODO if any trouble, see http://stackoverflow.com/questions/13590627/android-listview-headers
        mDrawerList.setAdapter(new NavigationArrayAdapter(this, null));
        mRemoteView.loadUrl("http://192.168.1.14:34340/ui/"
                            +mPlugins.getActivePluginName());
    }

}
