package com.codepath.chirrupy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.chirrupy.ChirrupyApplication;
import com.codepath.chirrupy.R;
import com.codepath.chirrupy.TweetsArrayAdapter;
import com.codepath.chirrupy.TwitterClient;
import com.codepath.chirrupy.misc.Helpers;
import com.codepath.chirrupy.models.Tweet;
import com.codepath.chirrupy.utilities.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsFragment extends Fragment {
	private TwitterClient      twitterClient;
	private ArrayList<Tweet>   tweets;
	private TweetsArrayAdapter tweetsAdapter;
	private ListView           lvTweets;
	private SwipeRefreshLayout srlTweets;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		tweets = new ArrayList<>();
		tweetsAdapter = new TweetsArrayAdapter(getActivity(), tweets);
		twitterClient = ChirrupyApplication.getRestClient();
		populateTweets(true);

		super.onCreate(savedInstanceState);
	}

	@Nullable
	public View onCreateView(LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {


		final View view = inflater.inflate(R.layout.fragment_tweets, container, false);
		lvTweets        = (ListView) view.findViewById(R.id.lvTweets);
		srlTweets       = (SwipeRefreshLayout) view.findViewById(R.id.srlTweets);

		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTweets(page == 0);
			}
		});

		tweetsAdapter.clear();
		lvTweets.setAdapter(tweetsAdapter);

		srlTweets.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateTweets(true);
			}
		});

		return view;
	}

	protected void resetTimeline() {
		Tweet.LastLowestId = Long.MAX_VALUE;
		tweetsAdapter.clear();
	}

	public abstract void populateTweets(boolean reset);

	protected TwitterClient getTwitterClient() {
		return twitterClient;
	}

	protected class TwitterResponseHandler extends JsonHttpResponseHandler {
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
			final List<Tweet> tweets = Tweet.FromJsonArray(json);
			tweetsAdapter.addAll(tweets);

			if (srlTweets != null) {
				srlTweets.setRefreshing(false);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			Toast.makeText(getActivity(), errorResponse.toString(), Toast.LENGTH_SHORT).show();
			Log.d(Helpers.LOG_TAG, "TwitterResponseHandler*ERROR (" + statusCode + "): " + errorResponse);
		}
	}
 }
