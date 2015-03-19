package com.codepath.ipix;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.ipix.ipix.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotosActivity extends Activity {

	private static final String CLIENT_ID           = "7c4989eaf6344d9d9171b15bed107466";
	private static final String IG_POPULAR_URL_BASE = "https://api.instagram.com/v1/media/popular?client_id=";

	private final ArrayList<InstagramMedia> _mediaList     = new ArrayList<>();
	private       MediaStreamAdapter        _mediaAdapter  = null;
	private       SwipeRefreshLayout        swipeContainer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);

		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

		swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				fetchPopularPhotos();
			}
		});

		swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		_mediaAdapter = new MediaStreamAdapter(this, _mediaList);

		final ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
		lvPhotos.setAdapter(_mediaAdapter);

		fetchPopularPhotos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_photos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void fetchPopularPhotos() {
		final String url = IG_POPULAR_URL_BASE + CLIENT_ID;
		final AsyncHttpClient client = new AsyncHttpClient();

		client.get(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d(Constants.LOG_TAG, response.toString());

				_mediaAdapter.clear();

				try {
					final JSONArray items = response.getJSONArray("data");

					for (int index = 0; index < items.length(); ++index) {
						final JSONObject json = items.getJSONObject(index);
						final InstagramMedia media = new InstagramMedia(json);
						_mediaList.add(media);
					}
				} catch (JSONException jex) {
					Toast.makeText(getApplicationContext(), jex.getMessage(), Toast.LENGTH_LONG).show();
					jex.printStackTrace();
				}

				_mediaAdapter.notifyDataSetChanged();
				swipeContainer.setRefreshing(false);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Log.e(Constants.LOG_TAG, responseString);
			}
		});
	}

	/*private static InstagramMedia ToInstagramMedia(JSONObject dataItem) throws JSONException {
		final JSONObject captionTag = dataItem.optJSONObject("caption");
		// if (captionTag == null) { captionTag = new JSONObject("{ text: \"????\" "); }

		final JSONObject userTag = dataItem.getJSONObject("user");
		final JSONObject likesTag = dataItem.getJSONObject("likes");

		final JSONObject image = dataItem.getJSONObject("images").getJSONObject("standard_resolution");
		final String url = image.getString("url");
		final int height = image.getInt("height");

		final String typeText = dataItem.getString("type");
		final InstagramMedia.Type type = typeText.equalsIgnoreCase("image")
				? InstagramMedia.Type.IMAGE
				: InstagramMedia.Type.VIDEO;

		final InstagramMedia media = new InstagramMedia(type, url);
		media.caption = captionTag.getString("text");
		media.author = userTag.getString("username");
		media.likesCount = likesTag.getInt("count");

		return media;
	}*/
}