package com.codepath.chirrupy.fragments;

public class MentionsFragment extends TweetsFragment {
	@Override
	public void populateTweets(boolean reset) {
		if (reset) {
			resetTimeline();
		}

		getTwitterClient().getMentionsTimeline(new TwitterResponseHandler());
	}
}
