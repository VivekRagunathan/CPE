package com.codepath.chirrupy.fragments;

import android.support.v4.app.Fragment;

public class MentionsFragment extends TweetsFragment {
	@Override
	public void populateTweets(boolean reset) {
		if (reset) {
			resetTimeline();
		}

		getTwitterClient().getMentionsTimeline(new TwitterResponseHandler());
	}
}
