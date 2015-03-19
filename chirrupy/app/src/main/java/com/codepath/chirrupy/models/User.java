package com.codepath.chirrupy.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Table(name = "users")
public class User extends Model implements Parcelable {
	public static final  String NAME                 = "name";
	public static final  String ID                   = "id";
	public static final  String SCREEN_NAME          = "screen_name";
	public static final  String PROFILE_IMAGE_URL    = "profile_image_url";
	private static final String DESCRIPTION          = "description";
	private static final String FOLLOWERS_COUNT      = "followers_count";
	private static final String FRIENDS_COUNT        = "friends_count";
	private static final String PROFILE_BG_IMAGE_URL = "profile_background_image_url";
	private static final String TWEETS_COUNT         = "statuses_count";

	@Column(name = "uid", unique = true, notNull = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;

	@Column(name = "name")
	private String name;

	@Column(name = "screen_name")
	private String screenName;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Column(name = "bgImgUrl")
	private String profileBackgroundImageUrl;

	@Column(name = "description")
	private String description;

	@Column(name = "num_tweets")
	private int noOfTweets;

	@Column(name = "num_followers")
	private int noOfFollowers;

	@Column(name = "num_friends")
	private int noOfFriends;

	public User() {
		super();
	}

	private User(Parcel parcel) {
		name = parcel.readString();
		uid = parcel.readLong();
		screenName = parcel.readString();
		profileImageUrl = parcel.readString();
		description = parcel.readString();
		noOfTweets = parcel.readInt();
		noOfFollowers = parcel.readInt();
		noOfFriends = parcel.readInt();
		profileBackgroundImageUrl = parcel.readString();
	}

	public User(long id, String name, String screenName, String imageUrl) {
		this.uid = id;
		this.name = name;
		this.screenName = screenName;
		this.profileImageUrl = imageUrl;
	}

	// region Parcelable Interface Implementation

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(name);
		parcel.writeLong(uid);
		parcel.writeString(screenName);
		parcel.writeString(profileImageUrl);
		parcel.writeString(description);
		parcel.writeInt(noOfTweets);
		parcel.writeInt(noOfFollowers);
		parcel.writeInt(noOfFriends);
		parcel.writeString(profileBackgroundImageUrl);
	}

	// endregion

	public static User FromJson(JSONObject source) {
		final User user = new User();
		try {
			user.name                       = source.getString(NAME);
			user.uid                        = source.getLong(ID);
			user.screenName                 = source.getString(SCREEN_NAME);
			user.profileImageUrl            = source.getString(PROFILE_IMAGE_URL);
			user.description                = source.getString(DESCRIPTION);
			user.noOfTweets                 = source.getInt(TWEETS_COUNT);
			user.noOfFollowers              = Integer.valueOf(source.getString(FOLLOWERS_COUNT));
			user.noOfFriends                = Integer.valueOf(source.getString(FRIENDS_COUNT));
			user.profileBackgroundImageUrl  = source.getString(PROFILE_BG_IMAGE_URL);
			user.save();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	// region DB Access Methods

	public static User QueryById(long id) {
		return new Select().from(User.class).where("uid = ?", id).executeSingle();
	}

	public static List<User> QueryRecentItems() {
		return QueryRecentItems(300);
	}

	public static List<User> QueryRecentItems(int count) {
		return new Select()
				.from(User.class)
				.orderBy("uid DESC")
				.limit(count)
				.execute();
	}

	// endregion

	// region Public Getter Methods

	public String getName() {
		return name;
	}

	public long getUserId() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public int getNoOfFollowers() {
		return noOfFollowers;
	}

	public int getNoOfFriends() {
		return noOfFriends;
	}

	public int getNoOfTweets() { return noOfTweets; }

	// endregion
}
