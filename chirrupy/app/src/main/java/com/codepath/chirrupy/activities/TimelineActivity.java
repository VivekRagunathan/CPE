package com.codepath.chirrupy.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.chirrupy.ChirrupyApplication;
import com.codepath.chirrupy.TweetsPagerAdapter;
import com.codepath.chirrupy.fragments.TimelineFragment;
import com.codepath.chirrupy.misc.ActivityRequestCodes;
import com.codepath.chirrupy.misc.Helpers;
import com.codepath.chirrupy.models.User;
import com.codepath.chirrupy.utilities.EndlessScrollListener;
import com.codepath.chirrupy.R;
import com.codepath.chirrupy.TweetsArrayAdapter;
import com.codepath.chirrupy.TwitterClient;
import com.codepath.chirrupy.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

	private TwitterClient twitterClient;
	private User          loggedInUser;

	private ArrayList<Tweet>   tweets;
	private TweetsArrayAdapter tweetsAdapter;
	private ListView           lvTweets;
	private SwipeRefreshLayout rlTimeline;
	private TimelineFragment timelineFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		Helpers.StyleActionBar(getSupportActionBar());

		twitterClient = ChirrupyApplication.getRestClient();

		twitterClient.getUserProfile(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				loggedInUser = User.FromJson(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				Toast.makeText(getApplicationContext(), "Failed to get user details", Toast.LENGTH_SHORT).show();
				Log.d(Helpers.LOG_TAG, "Failed to get user details: " + errorResponse);
				throwable.printStackTrace();
			}
		});

		timelineFragment = new TimelineFragment();

		final ViewPager viewPager               = (ViewPager) findViewById(R.id.viewPager);
		final TweetsPagerAdapter pagerAdapter   = new TweetsPagerAdapter(getSupportFragmentManager(), timelineFragment);
		viewPager.setAdapter(pagerAdapter);

		final PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setViewPager(viewPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch (id) {
			case R.id.action_compose: {
				final Intent intent = new Intent(this, ComposeActivity.class);
				intent.putExtra("user", loggedInUser);
				startActivityForResult(intent, ActivityRequestCodes.Compose.getValue());
				break;
			}

			case R.id.action_profile: {
				final Intent intent = new Intent(this, ProfileActivity.class);
				intent.putExtra("user", loggedInUser);
				startActivityForResult(intent, ActivityRequestCodes.Profile.getValue());
				break;
			}
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ActivityRequestCodes.Compose.getValue() && resultCode == RESULT_OK) {
			timelineFragment.populateTweets(true);
			return;
		}

		/*if (requestCode == ActivityRequestCodes.Profile.getValue() && resultCode == RESULT_OK) {
			return;
		}*/
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "Bye ...", Toast.LENGTH_SHORT).show();
		moveTaskToBack(true);
		super.onBackPressed();
	}

	private void resetTimeline() {
		Tweet.LastLowestId = Long.MAX_VALUE;
		tweetsAdapter.clear();
	}

	private void populateTimeline(boolean reset /*, int page*/) {
		if (reset) {
			resetTimeline();
		}

		twitterClient.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
				Log.d(Helpers.LOG_TAG, "{getHomeTimeline}: " + json.toString());
				final ArrayList<Tweet> items = Tweet.FromJsonArray(json);
				tweetsAdapter.addAll(items);

				if (rlTimeline != null) {
					rlTimeline.setRefreshing(false);
				}
			}

			@Override
			public void onFailure(int statusCode,
			                      Header[] headers,
			                      Throwable throwable,
			                      JSONObject errorResponse) {
				Log.d(Helpers.LOG_TAG, "getHomeTimeline FAILED! " + statusCode + ", " + errorResponse);
			}
		});
	}
}
