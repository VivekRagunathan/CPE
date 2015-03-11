package com.codepath.chirrupy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.chirrupy.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

	private TwitterClient      twitterClient;
	private ArrayList<Tweet>   tweets;
	private TweetsArrayAdapter tweetsAdapter;
	private ListView           lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		/*final ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(0x55ACEE));
		//actionBar.setBackgroundDrawable(new ColorDrawable(0XFF00DDED));
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);*/
		

		twitterClient = ChirrupyApplication.getRestClient();

		tweets          = new ArrayList<>();
		tweetsAdapter   = new TweetsArrayAdapter(this, tweets);
		lvTweets        = (ListView) findViewById(R.id.lvTweets);
		lvTweets.setAdapter(tweetsAdapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				populateTimeline(page);
				// or customLoadMoreDataFromApi(totalItemsCount);
			}
		});

		populateTimeline(25);
	}

	private void populateTimeline(int page) {
		twitterClient.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
				Log.d("DEBUG", "{getHomeTimeline}: " + json.toString());
				final ArrayList<Tweet> items = Tweet.FromJsonArray(json);
				tweetsAdapter.addAll(items);
			}

			@Override
			public void onFailure(int statusCode,
			                      Header[] headers,
			                      Throwable throwable,
			                      JSONObject errorResponse) {
				Log.d("DEBUG", "getHomeTimeline FAILED! " + statusCode + ", " + errorResponse);
			}
		});
	}

	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	private String readResourceFile(String name) {
		try {
			/*getResources().openRawResource(R.)
			InputStream in_s = res.openRawResource(R.raw.raw.help);

			byte[] b = new byte[in_s.available()];
			in_s.read(b);
			txtHelp.setText(new String(b));*/
		} catch (Exception e) {
			// e.printStackTrace();
//			txtHelp.setText("Error: can't show help.");
		}
		
		return "";
		
	} 


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch (id) {
			case R.id.action_settings:
				break;
			
			case R.id.action_compose:
				Toast.makeText(getApplicationContext(), "Under construction", Toast.LENGTH_SHORT).show();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
