package com.codepath.chirrupy.fragments;

public class TimelineFragment extends TweetsFragment {
	@Override
	public void populateTweets(boolean reset) {
		if (reset) {
			resetTimeline();
		}

		getTwitterClient().getHomeTimeline(new TwitterResponseHandler());
	}
}