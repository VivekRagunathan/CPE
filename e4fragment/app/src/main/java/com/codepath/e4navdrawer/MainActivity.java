package com.codepath.e4navdrawer;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.e4navdrawer.fragments.AboutFragment;
import com.codepath.e4navdrawer.fragments.HomeFragment;


public class MainActivity extends ActionBarActivity {

	private FragmentNavigationDrawer ndFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ndFragment = (FragmentNavigationDrawer) findViewById(R.id.ndFragment);
		final ListView lvDrawer = (ListView) findViewById(R.id.lvDrawer);
		ndFragment.setupDrawerConfiguration(lvDrawer, toolbar, R.layout.drawer_nav_item, R.id.flContent);

		ndFragment.addNavItem("Home", R.drawable.ic_one, "Home Fragment", HomeFragment.class);
		ndFragment.addNavItem("Settings", R.drawable.ic_two, "Home Fragment", HomeFragment.class);
		ndFragment.addNavItem("About", R.drawable.ic_three, "About Fragment", AboutFragment.class);

		if (savedInstanceState == null) {
			ndFragment.selectDrawerItem(0);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		ndFragment.getDrawerToggle().syncState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		if (ndFragment.isDrawerOpen()) {
			//menu.findItem(R.id.mi_test).setVisible(false);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int id = item.getItemId();

		if (ndFragment.getDrawerToggle().onOptionsItemSelected(item)) {
			return true;
		}

		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		ndFragment.getDrawerToggle().onConfigurationChanged(newConfig);
	}
}
