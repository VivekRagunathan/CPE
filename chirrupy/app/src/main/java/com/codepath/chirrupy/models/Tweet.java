package com.codepath.chirrupy.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "tweets")
public class Tweet extends Model {
	public static long LastLowestId = Long.MAX_VALUE;

	@Column(name = "tid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE, notNull = true)
	private long tid;

	@Column(name = "body")
	private String body;

	@Column(name = "created_at")
	private String createdAt;

	@Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
	private User user;

	public Tweet() {
		super();
	}

	public String getBody() {
		return body;
	}

	public long getTweetId() {
		return tid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public static Tweet FromJson(JSONObject source) {
		final Tweet tweet = new Tweet();

		try {
			final JSONObject user = source.getJSONObject("user");

			tweet.body = source.getString("text");
			tweet.tid = source.getLong("id");
			tweet.createdAt = source.getString("created_at");
			tweet.user = User.FromJson(user);

			final long tweetId      = tweet.getTweetId();

			if (tweetId == LastLowestId) {
				return null;
			}

			if (tweetId < LastLowestId) {
				LastLowestId = tweetId;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return tweet;
	}
	
	public static ArrayList<Tweet> FromJsonArray(JSONArray source) {
		ArrayList<Tweet> tweets = new ArrayList<>();

		for (int index = 0; index < source.length(); ++index) {
			try {
				final JSONObject item = source.getJSONObject(index);
				final Tweet tweet = FromJson(item);
				if (tweet != null) {
					tweets.add(tweet);
				}
			} catch (JSONException jex) {
				jex.printStackTrace();
			}
		}
		
		return tweets;
	}

	public static Tweet QueryById(long id) {
		return new Select().from(Tweet.class).where("uid = ?", id).executeSingle();
	}

	public static List<Tweet> QueryRecentItems() {
		return QueryRecentItems(300);
	}

	public static List<Tweet> QueryRecentItems(int count) {
		return new Select()
				.from(Tweet.class)
				.orderBy("uid DESC")
				.limit(count)
				.execute();
	}

	public static List<Tweet> DeleteById(String id) {
		return new Delete()
				.from(Tweet.class)
				.where("tid = ?", id)
				.execute();
	}
}
