package com.github.awerem.aweremandroid;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.awerem.aweremandroid.navigation.Item;
import com.github.awerem.aweremandroid.navigation.NavigationArrayAdapter;
import com.github.awerem.aweremandroid.navigation.NavigationArrayAdapter.RowType;
import com.github.awerem.aweremandroid.navigation.RemoteItem;
import com.github.awerem.aweremandroid.plugins.PluginsManager;
import com.github.awerem.aweremandroid.plugins.onPluginsInfoLoadedListener;
import com.github.awerem.aweremandroid.utils.Utils;
import com.github.awerem.aweremandroid.web.RemoteJSInterface;
import com.github.awerem.aweremandroid.web.RemoteWebChrome;
import com.github.awerem.aweremandroid.web.RemoteWebClient;

public class RemoteActivity extends Activity implements
		onPluginsInfoLoadedListener, ListView.OnItemClickListener
{
	private static final String DEBUG_TAG = "RemoteActivity";
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private WebView mRemoteView;
	private PluginsManager mPlugins;
	private ArrayList<Item> itemsList = null;
	private String mIp = null;
	private PollManager pollmanager;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent data = getIntent();
		mIp = data.getStringExtra("ip");
		setContentView(R.layout.activity_remote);
		initDrawer();
		pollmanager = new PollManager(this, "http://" + mIp
				+ ":34340/core?get=infos");
		mRemoteView = (WebView) findViewById(R.id.remote_view);
		mRemoteView.getSettings().setJavaScriptEnabled(true);
		mRemoteView.setWebChromeClient(new RemoteWebChrome());
		mRemoteView.setWebViewClient(new RemoteWebClient(mIp));
		mPlugins = new PluginsManager(this, mIp);
		mPlugins.gatherPlugins();
	}

	public void initDrawer()
	{
		mDrawerLayout = (DrawerLayout) findViewById(R.id.mainLayout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
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

		mDrawerList.setOnItemClickListener(this);

	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
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
	public void onPluginsInfoLoaded(boolean navDrawer, boolean remoteView)
	{
		if (navDrawer)
			updateNavigationDrawer();
		if (remoteView)
			updateRemoteView();
	}

	private void updateRemoteView()
	{
		mRemoteView.loadUrl("http://" + mIp + ":34340/ui/"
				+ mPlugins.getActivePluginName());
		mRemoteView.addJavascriptInterface(
				new RemoteJSInterface(mPlugins.getActivePluginName(), mIp,
						pollmanager, mRemoteView), "awerem");
	}

	private void updateNavigationDrawer()
	{
		Log.i(DEBUG_TAG, "updateNavigationDrawer");
		itemsList = Utils.createNavList(mPlugins.getPlugins(), this, mIp);
		mDrawerList.setAdapter(new NavigationArrayAdapter(this, itemsList));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		if (itemsList.get(position).getViewType() == RowType.LIST_ITEM
				.ordinal())
		{
			RemoteItem rem = (RemoteItem) itemsList.get(position);
			mPlugins.setActive(rem.getPlugin().getName());
			updateRemoteView();
			mDrawerList.setItemChecked(position, true);
			setTitle(mPlugins.getActivePluginTitle());
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	public void triggerPluginGathering()
	{
		mPlugins.gatherPlugins();
	}

	public void onConnectionLost()
	{
		Toast.makeText(this, R.string.connection_lost, Toast.LENGTH_LONG)
				.show();
		startActivity(new Intent(this, PairingActivity.class));
		finish();
	}
}
