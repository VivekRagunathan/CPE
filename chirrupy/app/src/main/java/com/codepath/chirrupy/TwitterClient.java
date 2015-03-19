package com.codepath.chirrupy;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.chirrupy.misc.TwitterConstants;
import com.codepath.chirrupy.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS       = TwitterApi.class;
	public static final String               REST_URL             = TwitterConstants.BASE_URL;
	public static final String               REST_CONSUMER_KEY    = TwitterConstants.CONSUMER_KEY;
	public static final String               REST_CONSUMER_SECRET = TwitterConstants.CONSUMER_SECRET;
	public static final String               REST_CALLBACK_URL    = TwitterConstants.CALLBACK_URL;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		final String apiUrl = getApiUrl(TwitterConstants.HOME_TIMELINE_URL);
		final RequestParams params = new RequestParams(RequestParameters.COUNT, 25);

		if (Tweet.LastLowestId != Long.MAX_VALUE) {
			params.put(RequestParameters.MAX_ID, Tweet.LastLowestId);
		}

		getClient().get(apiUrl, params, handler);
	}

	public void getUserTimeLine(String screenName, AsyncHttpResponseHandler handler) {
		final String apiUrl = getApiUrl(TwitterConstants.USER_TIMELINE_URL);
		final RequestParams params = new RequestParams(RequestParameters.SCREEN_NAME, screenName);
		client.get(apiUrl, params, handler);
	}

	public void getMentionsTimeline(JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl(TwitterConstants.MENTIONS_TIMELINE);
		client.get(apiUrl, new RequestParams(), handler);
	}

	public void getUserProfile(AsyncHttpResponseHandler handler) {
		final String apiUrl = getApiUrl(TwitterConstants.USER_PROFILE);
		getClient().get(apiUrl, new RequestParams(), handler);
	}

	public void postTweet(String message, AsyncHttpResponseHandler handler) {
		final String apiUrl = getApiUrl(TwitterConstants.POST_TWEET);
		getClient().post(apiUrl, new RequestParams(RequestParameters.STATUS, message), handler);
	}

	public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl(TwitterConstants.GET_USERS);
		RequestParams params = new RequestParams(RequestParameters.SCREEN_NAME, screenName);
		client.get(apiUrl, params, handler);
	}

	public static class RequestParameters{
		static final String COUNT       = "count";
		static final String STATUS      = "status";
		static final String MAX_ID      = "max_id";
		static final String SINCE_ID    = "since_id";
		static final String SCREEN_NAME = "screen_name";
	}
}