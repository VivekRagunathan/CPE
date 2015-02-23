package com.codepath.ipix.model;

import android.util.Log;

import com.codepath.ipix.ipix.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;

public class InstagramMedia {
	private static final String CONTENT_MISSING = ""; //"?*?";

	private JSONObject source;

	public InstagramMedia() {}

	public InstagramMedia(JSONObject source) throws JSONException {
		this.source = source;
	}

	public Type type() {
		final String type = this.source.optString("type");
		return "image".equalsIgnoreCase(type) ? Type.IMAGE : Type.VIDEO;
	}

	public String author() {
		final JSONObject user = this.source.optJSONObject("user");
		return user == null ? CONTENT_MISSING : user.optString("username");
	}

	public String authorPicture() {
		final JSONObject user = this.source.optJSONObject("user");
		return user == null ? CONTENT_MISSING : user.optString("profile_picture");
	}

	public String captionText() {
		final JSONObject caption = this.source.optJSONObject("caption");
		return caption == null ? CONTENT_MISSING : caption.optString("text");
	}

	public ImageInfo imageInfo() {
		final JSONObject image = this.source.optJSONObject("images").optJSONObject("standard_resolution");
		final String url = image.optString("url");
		final int height = image.optInt("height", 0);
		final int width = image.optInt("width", 0);

		return new ImageInfo(url, width, height);
	}

	public int likesCount() {
		final JSONObject likes = this.source.optJSONObject("likes");
		return likes.optInt("count", 0);
	}

	public Comments comments(int requiredNoOfComments) {
		final JSONObject comments   = this.source.optJSONObject("comments");

		final ArrayList<Comment> output = new ArrayList<>();

		final JSONArray data    = comments.optJSONArray("data");
		final int count         = comments.optInt("count");
		if (requiredNoOfComments < 0 || requiredNoOfComments > count) { requiredNoOfComments = count; }

		for (int index = 0; index < requiredNoOfComments; ++index) {
			final JSONObject source = data.optJSONObject(index);
			final Comment comment   = new Comment(source);
			output.add(comment);
		}

		final Comment[] result = new Comment[output.size()];
		output.toArray(result);

		return new Comments(result, count);
	}

	public enum Type {
		IMAGE,
		VIDEO
	}

	public class ImageInfo {
		public String url;
		public int width = 0;
		public int height = 0;

		public ImageInfo(String url, int width, int height) {
			this.url = url;
			this.width = width;
			this.height = height;

		}
	}

	public class Comments {
		public int count;
		public Comment[] comments;

		public Comments(Comment[] comments) {
			this(comments, -1);
		}

		public Comments(Comment[] comments, int count) {
			this.comments = comments;
			this.count = count > 0 ? count : (comments == null ? 0 : comments.length);
		}
	}

	public static class Comment {
		public static final Comment Dummy = new Comment(new JSONObject());

		private JSONObject source;

		public Comment(JSONObject source) {
			this.source = source;
		}

		public String createdTime() {
			return this.source.optString("created_time");
		}

		public String by() {
			final JSONObject from = this.source.optJSONObject("from");
			return from == null ? "" : from.optString("username");
		}

		public String text() {
			return this.source.optString("text");
		}

		/*"from": {
			"full_name": "melaniahelena",
					"id": "1058105230",
					"profile_picture": "https://igcdn-photos-g-a.akamaihd.net/hphotos-ak-xaf1/t51.2885-19/10919676_921694887854758_676551808_a.jpg",
					"username": "melaniahelena"
		},
		"id": "926354264534085743"*/
	}
}
