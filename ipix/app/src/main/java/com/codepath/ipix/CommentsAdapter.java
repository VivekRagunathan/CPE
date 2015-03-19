package com.codepath.ipix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.ipix.ipix.R;

public class CommentsAdapter extends ArrayAdapter<InstagramMedia.Comment> {
	public CommentsAdapter(Context context, InstagramMedia.Comment[] dataSource) {
		super(context, android.R.layout.simple_list_item_2, dataSource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final InstagramMedia.Comment comment = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.comments, parent, false);
		}

		return convertView;
	}
}
