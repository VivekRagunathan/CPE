package com.codepath.chirrupy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.chirrupy.activities.ProfileActivity;
import com.codepath.chirrupy.misc.Helpers;
import com.codepath.chirrupy.models.Tweet;
import com.codepath.chirrupy.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

	private static class TweetsViewHolder {
		ImageView ivProfileImage;
		TextView  tvUserName;
		TextView  tvHandle;
		TextView  tvWhen;
		TextView  tvBody;

		public TweetsViewHolder(View convertView) {
			this.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
			this.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
			this.tvHandle = (TextView) convertView.findViewById(R.id.tvHandle);
			this.tvWhen = (TextView) convertView.findViewById(R.id.tvWhen);
			this.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
		}
	}

	public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, android.R.layout.simple_list_item_1, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Tweet tweet = getItem(position);

		TweetsViewHolder viewHolder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
			viewHolder = new TweetsViewHolder(convertView);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (TweetsViewHolder) convertView.getTag();
		}

		/*final ImageView ivProfileImage  = (ImageView) convertView.findViewById(R.id.ivProfileImage);
		final TextView tvUserName       = (TextView) convertView.findViewById(R.id.tvUserName);
		final TextView tvHandle         = (TextView) convertView.findViewById(R.id.tvHandle);
		final TextView tvWhen           = (TextView) convertView.findViewById(R.id.tvWhen);
		final TextView tvBody           = (TextView) convertView.findViewById(R.id.tvBody);*/
		
		final User user = tweet.getUser();
		viewHolder.tvUserName.setText(user.getScreenName());
		viewHolder.tvHandle.setText("@" + user.getName());
		viewHolder.tvWhen.setText(Helpers.GetRelativeTimeAgo(tweet.getCreatedAt()));
		viewHolder.tvBody.setText(tweet.getBody());
		viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);

		Picasso.with(getContext())
				.load(user.getProfileImageUrl())
				.into(viewHolder.ivProfileImage);

		viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(getContext(), ProfileActivity.class);
				//intent.putExtra(ProfileActivity.SCREEN_NAME, user.getScreenName());
				intent.putExtra(ProfileActivity.USER, user);
				getContext().startActivity(intent);
			}
		});
		
		return convertView;
	}
}
