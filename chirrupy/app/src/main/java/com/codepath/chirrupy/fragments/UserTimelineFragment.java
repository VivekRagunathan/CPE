package com.codepath.chirrupy.fragments;

import android.os.Bundle;

public class UserTimelineFragment extends TweetsFragment {

	private static final String SCREEN_NAME = "screen_name";

	public UserTimelineFragment() {
	}

	public static UserTimelineFragment NewInstance(String screenName) {
		final UserTimelineFragment fragment = new UserTimelineFragment();
		final Bundle bundle = new Bundle();
		bundle.putString(SCREEN_NAME, screenName);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void populateTweets(boolean reset) {
		if (reset) {
			resetTimeline();
		}

		final String screenName = getArguments().getString(SCREEN_NAME);
		getTwitterClient().getUserTimeLine(screenName, new TwitterResponseHandler());
	}
}
