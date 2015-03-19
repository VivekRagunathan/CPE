package com.codepath.chirrupy.activities;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.chirrupy.ChirrupyApplication;
import com.codepath.chirrupy.R;
import com.codepath.chirrupy.TwitterClient;
import com.codepath.chirrupy.fragments.UserTimelineFragment;
import com.codepath.chirrupy.misc.Helpers;
import com.codepath.chirrupy.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringUtils;

public class ProfileActivity extends ActionBarActivity {
	public static final String SCREEN_NAME = "screen_name";
	public static final String USER        = "user";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		final User user                     = getIntent().getParcelableExtra(USER);
		final String screenName             = user.getScreenName(); // getIntent().getStringExtra(SCREEN_NAME);

		if (user != null) {
			populateProfileHeader(user);
		}

		if (savedInstanceState == null) {
			final UserTimelineFragment userTimelineFragment = UserTimelineFragment.NewInstance(screenName);
			final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.flContainer, userTimelineFragment);
			ft.commit();
		}

		Helpers.StyleActionBar(getSupportActionBar());
	}

	private void populateProfileHeader(User user) {
		final TextView tvName               = (TextView) findViewById(R.id.tvUserName);
		final TextView tvScreenName         = (TextView) findViewById(R.id.tvHandle);
		final TextView tvTagLine            = (TextView) findViewById(R.id.tvTagLine);
		final TextView tvTweetsCount        = (TextView) findViewById(R.id.tvTweetsCount);
		final TextView tvFollowers          = (TextView) findViewById(R.id.tvFollowers);
		final TextView tvFollowing          = (TextView) findViewById(R.id.tvFollowings);
		final ImageView ivProfileImage      = (ImageView) findViewById(R.id.ivProfileImage);
		final RelativeLayout rlUserHeader   = (RelativeLayout) findViewById(R.id.rlUserHeader);

		rlUserHeader.setTag(new Target() {
			@Override
			public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
				if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					rlUserHeader.setBackgroundDrawable(new BitmapDrawable(bitmap));
					return;
				}

				rlUserHeader.setBackground(new BitmapDrawable(getResources(), bitmap));
			}

			@Override
			public void onBitmapFailed(Drawable errorDrawable) {
				Toast.makeText(getApplicationContext(), "OnBitmapFailed", Toast.LENGTH_SHORT).show();
				Log.d(Helpers.LOG_TAG, "*** onBitmapFailed ...");
			}

			@Override
			public void onPrepareLoad(Drawable placeHolderDrawable) {
				Log.d(Helpers.LOG_TAG, "*** onPrepareLoad ...");
			}
		});

		tvName.setText(user.getName());
		tvScreenName.setText("@" + user.getScreenName());
		tvTagLine.setText((user.getDescription()));
		tvTweetsCount.setText(user.getNoOfTweets() + "\nTWEETS");
		tvFollowers.setText(user.getNoOfFollowers() + "\nFOLLOWERS");
		tvFollowing.setText(user.getNoOfFriends() + "\nFOLLOWING");
		Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

		final String bgUrl = user.getProfileBackgroundImageUrl();

		if (!StringUtils.isBlank(bgUrl)) {
			Picasso.with(this)
					.load(bgUrl)
					.resize(128, 128)
					.into((Target) rlUserHeader.getTag());
		}
	}
}
