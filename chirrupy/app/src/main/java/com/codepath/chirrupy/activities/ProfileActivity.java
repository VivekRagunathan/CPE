package com.codepath.chirrupy.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.chirrupy.ChirrupyApplication;
import com.codepath.chirrupy.R;
import com.codepath.chirrupy.TwitterClient;
import com.codepath.chirrupy.fragments.UserTimelineFragment;
import com.codepath.chirrupy.misc.Helpers;
import com.codepath.chirrupy.models.User;
import com.squareup.picasso.Picasso;

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
		final TextView tvName           = (TextView) findViewById(R.id.tvUserName);
		final TextView tvScreenName     = (TextView) findViewById(R.id.tvHandle);
		final TextView tvTagLine        = (TextView) findViewById(R.id.tvTagLine);
		final TextView tvFollowers      = (TextView) findViewById(R.id.tvFollowers);
		final TextView tvFollowing      = (TextView) findViewById(R.id.tvFollowings);
		final ImageView ivProfileImage  = (ImageView) findViewById(R.id.ivProfileImage);

		tvName.setText(user.getName());
		tvScreenName.setText("@" + user.getScreenName());
		tvTagLine.setText((user.getDescription()));
		tvFollowers.setText(user.getNoOfFollowers() + " Followers");
		tvFollowing.setText(user.getNoOfFriends() + " Following");
		Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
	}
}
