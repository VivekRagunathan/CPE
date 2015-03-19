package com.codepath.chirrupy.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.chirrupy.ChirrupyApplication;
import com.codepath.chirrupy.misc.Helpers;
import com.codepath.chirrupy.R;
import com.codepath.chirrupy.TwitterClient;
import com.codepath.chirrupy.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {
	public static final String USER            = "user";
	public static final int    MAX_BODY_LENGTH = 140;

	private TwitterClient twitterClient;
	private TextView      tvRemainingLength;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		twitterClient = ChirrupyApplication.getRestClient();
		Helpers.StyleActionBar(getSupportActionBar());

		final User loggedInUser = getIntent().getParcelableExtra(USER);
		initializeUI(loggedInUser);

		//loadUserProfile();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_compose, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int id = item.getItemId();

		switch (id) {
			case R.id.action_tweet:
				final TextView etBody = (TextView) findViewById(R.id.etBody);
				final String message = etBody.getText().toString();
				tweetMessage(message);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initializeUI(User user) {
		Log.d(Helpers.LOG_TAG, "Initializing user interface ...");

		ImageView ivProfileImage    = (ImageView) findViewById(R.id.ivProfileImage);
		TextView tvUserName         = (TextView) findViewById(R.id.tvUserName);
		TextView tvHandle           = (TextView) findViewById(R.id.tvHandle);
		EditText etBody             = (EditText) findViewById(R.id.etBody);
		this.tvRemainingLength      = (TextView) findViewById(R.id.tvRemaining);

		tvUserName.setText("@" + user.getName());
		tvHandle.setText(user.getScreenName());
		etBody.setText("");

		ivProfileImage.setImageResource(0);
		Picasso.with(getApplicationContext())
				.load(user.getProfileImageUrl())
				.into(ivProfileImage);

		etBody.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				final int value = MAX_BODY_LENGTH - s.length();
				tvRemainingLength.setText(String.valueOf(value));
			}
		});
	}

	private void tweetMessage(String message) {
		if (StringUtils.isBlank(message)) {
			Toast.makeText(this, "Enter message ...", Toast.LENGTH_SHORT).show();
			return;
		}

		Toast.makeText(this, "Tweeting ...", Toast.LENGTH_SHORT).show();

		twitterClient.postTweet(message, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int i, Header[] headers, byte[] bytes) {
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
				final String message = "Failed to tweet! " + throwable.getMessage();
				Log.e(Helpers.LOG_TAG, message);
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	// region TO BE REMOVED

	private void loadUserProfile() {
		Toast.makeText(this, "Loading user information ...", Toast.LENGTH_SHORT).show();

		twitterClient.getUserProfile(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
				Log.d(Helpers.LOG_TAG, "getUserProfile: " + json);
				onFetchUserProfile(json);
			}

			@Override
			public void onFailure(int statusCode,
			                      Header[] headers,
			                      Throwable throwable,
			                      JSONObject errorResponse) {
				Log.d(Helpers.LOG_TAG, "getUserProfile FAILED! " + statusCode + ", " + errorResponse);
			}
		});
	}

	private void onFetchUserProfile(JSONObject json) {
		final User user = User.FromJson(json);
		cacheUserProfile(user);
		initializeUI(user);
	}

	private void cacheUserProfile(User user) {
		Log.d(Helpers.LOG_TAG, "Caching user profile...");
		final SharedPreferences sp              = getPreferences(Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor   = sp.edit();
		editor.putLong(User.ID, user.getUserId());
		editor.putString(User.NAME, user.getName());
		editor.putString(User.SCREEN_NAME, user.getScreenName());
		editor.putString(User.PROFILE_IMAGE_URL, user.getProfileImageUrl());
		editor.apply();
	}

	private User getUserFromCache() {
		final SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);

		return new User(sp.getLong(User.ID, -1),
				sp.getString(User.NAME, ""),
				sp.getString(User.SCREEN_NAME, ""),
				sp.getString(User.PROFILE_IMAGE_URL, ""));
	}

	// endregion
}
