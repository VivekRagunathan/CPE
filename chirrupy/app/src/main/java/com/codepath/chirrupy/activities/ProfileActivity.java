package com.codepath.chirrupy.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.chirrupy.ChirrupyApplication;
import com.codepath.chirrupy.R;
import com.codepath.chirrupy.TwitterClient;
import com.codepath.chirrupy.misc.Helpers;
import com.codepath.chirrupy.models.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends ActionBarActivity {
	private TwitterClient twitterClient;
	private User          user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		twitterClient = ChirrupyApplication.getRestClient();

//		String screenName = getIntent().getStringExtra("screen_name");
		user = getIntent().getParcelableExtra("user");

		if (user != null) {
			populateProfileHeader(user);
		} else {

		}

		if (savedInstanceState == null) {
			/*UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
			android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.flContainer, userTimelineFragment);
			ft.commit();*/
		}

		Helpers.StyleActionBar(getSupportActionBar());
	}

	private void populateProfileHeader(User user) {
		final TextView tvName           = (TextView) findViewById(R.id.tvUserName);
		final TextView tvScreenName     = (TextView) findViewById(R.id.tvScreenName);
		final TextView tvTagLine        = (TextView) findViewById(R.id.tvTagLine);
		final TextView tvFollowers      = (TextView) findViewById(R.id.tvFollowers);
		final TextView tvFollowing      = (TextView) findViewById(R.id.tvFollowings);
		final ImageView ivProfileImage  = (ImageView) findViewById(R.id.ivProfileImage);

        //ImageView ivUserHeaderBackground = (ImageView) findViewById(R.id.ivUserHeaderBackground);
		final RelativeLayout rlProfile = (RelativeLayout) findViewById(R.id.rlUserHeader);

		tvName.setText(user.getName());
		tvScreenName.setText("@" + user.getScreenName());
		tvTagLine.setText((user.getDescription()));
		tvFollowers.setText(user.getNoOfFollowers() + " Follower");
		tvFollowing.setText(user.getNoOfFriends() + " Following");
		Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
        //Picasso.with(this).load(user.getProfileBackgroundImageUrl()).centerCrop().fit().into(ivUserHeaderBackground);
	}
}
