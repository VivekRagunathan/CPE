package com.codepath.ipix.ipix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.ipix.model.InstagramMedia;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MediaStreamAdapter extends ArrayAdapter<InstagramMedia> {
	public MediaStreamAdapter(Context context, List<InstagramMedia> dataSource) {
		super(context, android.R.layout.simple_list_item_1, dataSource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final InstagramMedia media = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
		}

		final RelativeLayout rlCaptionBar = PrepareCaptionBar(convertView, media);
		PrepareLikesBar(convertView, media);
		PrepareCommentsBar(convertView, media);

		final ImageView ivAuthorPic = (ImageView) rlCaptionBar.findViewById(R.id.ivAuthorPic);
		ivAuthorPic.setImageResource(0);
		Picasso.with(getContext())
				.load(media.authorPicture())
				.fit()
				.centerCrop()
				.placeholder(R.drawable.ic_launcher)
				.into(ivAuthorPic);

		final ImageView ivPhoto                 = (ImageView) convertView.findViewById(R.id.ivPhoto);
		final InstagramMedia.ImageInfo image    = media.imageInfo();
		ivPhoto.setImageResource(0);
		Picasso.with(getContext())
				.load(image.url)
				.fit()
				.centerCrop()
				.placeholder(R.drawable.ic_launcher)
				.into(ivPhoto);

		return convertView;
	}

	private static RelativeLayout PrepareCaptionBar(View convertView, InstagramMedia media) {
		final RelativeLayout rlCaptionBar = (RelativeLayout) convertView.findViewById(R.id.rlCaptionBar);

		final TextView tvAuthor = (TextView) rlCaptionBar.findViewById(R.id.tvAuthor);
		tvAuthor.setText(media.author());

		final TextView tvCaption = (TextView) rlCaptionBar.findViewById(R.id.tvCaption);
		tvCaption.setText(media.captionText());
		return rlCaptionBar;
	}

	private static void PrepareLikesBar(View convertView, InstagramMedia media) {
		final LinearLayout llLikesBar   = (LinearLayout) convertView.findViewById(R.id.llLikesBar);
		final TextView tvLikes          = (TextView) llLikesBar.findViewById(R.id.tvLikesText);
		final String likesText          = String.format("%s likes", media.likesCount());
		tvLikes.setText(likesText);
	}

	private static void PrepareCommentsBar(View convertView, InstagramMedia media) {
		final RelativeLayout rlCommentsBar      = (RelativeLayout) convertView.findViewById(R.id.rlCommentsBar);
		final TextView tvViewAllComments        = (TextView) rlCommentsBar.findViewById(R.id.tvViewAllComments);
		final InstagramMedia.Comments comments  = media.comments(1);
		final String viewAllText = String.format("View all %d comments", comments.count);
		tvViewAllComments.setText(viewAllText);

		final TextView tvCommentedBy = (TextView) rlCommentsBar.findViewById(R.id.tvCommentedBy);
		final InstagramMedia.Comment comment = comments.comments.length == 0 ? InstagramMedia.Comment.Dummy : comments.comments[0];
		tvCommentedBy.setText(comment.by());

		final TextView tvComment = (TextView) rlCommentsBar.findViewById(R.id.tvComment);
		tvComment.setText(comment.text());
	}
}
