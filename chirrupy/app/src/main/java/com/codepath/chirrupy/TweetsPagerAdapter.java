package com.codepath.chirrupy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.chirrupy.fragments.MentionsFragment;
import com.codepath.chirrupy.fragments.TimelineFragment;

public class TweetsPagerAdapter extends FragmentPagerAdapter {
	private static final String Titles[] = { "Home", "Mentions" };

	private TimelineFragment tlFragment;

	public TweetsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public TweetsPagerAdapter(FragmentManager fm, TimelineFragment tlFragment) {
		super(fm);
		this.tlFragment = tlFragment;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:     return tlFragment; //new TimelineFragment();
			case 1:     return new MentionsFragment();
			default:    return null;
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return Titles[position];
	}

	@Override
	public int getCount() {
		return Titles.length;
	}
}
