package com.codepath.e4navdrawer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentNavigationDrawer extends DrawerLayout {

	private ActionBarDrawerToggle      drawerToggle;
	private ListView                   lvDrawer;
	private Toolbar                    toolbar;
	private NavDrawerListAdapter       drawerAdapter;
	private ArrayList<FragmentNavItem> drawerNavItems;
	private int                        drawerContainerRes;
	private ArrayList<NavDrawerItem>   navDrawerItems;

	public FragmentNavigationDrawer(Context context) {
		super(context);
	}

	public FragmentNavigationDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FragmentNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setupDrawerConfiguration(ListView drawerListView,
	                                     Toolbar toolbar,
	                                     int drawerItemRes,
	                                     int drawerContainerRes) {

		drawerNavItems = new ArrayList<>();
		navDrawerItems = new ArrayList<>();
		this.drawerContainerRes = drawerContainerRes;
		this.toolbar = toolbar;
		lvDrawer = drawerListView;
		lvDrawer.setAdapter(drawerAdapter);
		lvDrawer.setOnItemClickListener(new FragmentDrawerItemListener());

		drawerToggle = setupDrawerToggle();
		setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	private ActionBarDrawerToggle setupDrawerToggle() {
		return new ActionBarDrawerToggle(getActivity(), this, toolbar, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				// setTitle(getCurrentTitle());
				// call onPrepareOptionsMenu()
				getActivity().supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// setTitle("Navigate");
				// call onPrepareOptionsMenu()
				getActivity().supportInvalidateOptionsMenu();
			}
		};
	}

	public void addNavItem(String title, int icon, String windowTitle, Class<? extends Fragment> fragmentClass) {
		navDrawerItems.add(new NavDrawerItem(title, icon));
		drawerAdapter = new NavDrawerListAdapter(getActivity(), navDrawerItems);
		lvDrawer.setAdapter(drawerAdapter);
		drawerNavItems.add(new FragmentNavItem(windowTitle, fragmentClass));
	}

	public boolean isDrawerOpen() {
		return isDrawerOpen(lvDrawer);
	}

	public ActionBarDrawerToggle getDrawerToggle() {
		return drawerToggle;
	}

	private FragmentActivity getActivity() {
		return (FragmentActivity) getContext();
	}

	private ActionBar getSupportActionBar() {
		final ActionBarActivity activity = (ActionBarActivity) getActivity();
		return activity.getSupportActionBar();
	}

	private void setTitle(CharSequence title) {
		getSupportActionBar().setTitle(title);
	}

	public void selectDrawerItem(int position) {
		FragmentNavItem navItem = drawerNavItems.get(position);
		Fragment fragment = null;
		try {
			fragment = navItem.getFragmentClass().newInstance();
			Bundle args = navItem.getFragmentArgs();
			if (args != null) {
				fragment.setArguments(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(drawerContainerRes, fragment).commit();

		lvDrawer.setItemChecked(position, true);
		setTitle(navItem.getTitle());
		closeDrawer(lvDrawer);
	}


	private class FragmentDrawerItemListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectDrawerItem(position);
		}
	}

	private class FragmentNavItem {
		private String                    title;
		private Bundle                    fragmentArgs;
		private Class<? extends Fragment> fragmentClass;

		public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass) {
			this(title, fragmentClass, null);
		}

		public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass, Bundle args) {
			this.fragmentClass = fragmentClass;
			this.fragmentArgs = args;
			this.title = title;
		}

		public Class<? extends Fragment> getFragmentClass() {
			return fragmentClass;
		}

		public String getTitle() {
			return title;
		}

		public Bundle getFragmentArgs() {
			return fragmentArgs;
		}
	}
}
