package com.codepath.simplechat;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.codec.binary.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ChatActivity extends ActionBarActivity {
	private static final String                      PARSE_CLASS_NAME           = ChatMessage.class.getSimpleName();
	private static final String                      PARSE_META_TOKEN           = "__abhi__";
	private static final int                         CHAT_REFRESH_INTERVAL_SECS = 5;
	private static final String                      LOG_TAG                    = ChatApplication.class.getSimpleName();
	private static final ScheduledThreadPoolExecutor ChatScheduler              = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);

	private ListView                  lvMessages;
	private ArrayList<ChatMessage>    messages;
	private ArrayAdapter<ChatMessage> messagesAdapter;
	private volatile int messageSkipCount = 0;

	private Runnable rcmRunnable = new Runnable() {
		@Override
		public void run() {
			refreshChatMessages();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		messages        = new ArrayList<>();
		messagesAdapter = new ChatMessagesAdapter(this, messages);
		lvMessages      = (ListView) findViewById(R.id.lvMessages);
		lvMessages.setAdapter(messagesAdapter);

		final EditText etMessage = (EditText) findViewById(R.id.etMessage);

		etMessage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event.getKeyCode() != KeyEvent.KEYCODE_ENTER) { return false; }
//				if (actionId != EditorInfo.IME_ACTION_DONE) { return false; }
				findViewById(R.id.btnSend).performClick();
				return true;
			}
		});

		ScheduledFuture<?> periodicFuture = ChatScheduler.scheduleAtFixedRate(rcmRunnable,
				0,
				CHAT_REFRESH_INTERVAL_SECS,
				TimeUnit.SECONDS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_chat, menu);
		ParseAnonymousUtils.logIn(new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				Log.d(LOG_TAG, "@Login " + (e == null ? "FAILED!" : "success!"));
				Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
			}
		});
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
	
	public void OnClickSend(View view) {
		final EditText etMessage    = (EditText)findViewById(R.id.etMessage);
		final String message        = etMessage.getText().toString();
		if (Utils.isBlank(message)) { return; }

		final ParseObject parseObject = new ParseObject(PARSE_CLASS_NAME);
		parseObject.put("meta", PARSE_META_TOKEN);
		parseObject.put("message", message);
		Toast.makeText(getApplicationContext(), "Sending ...", Toast.LENGTH_SHORT).show();

		parseObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException pex) {
				if (pex != null) {
					Log.e(LOG_TAG, "Failed to send message. " + pex.getMessage());
					Toast.makeText(getApplicationContext(), "Failed to send message!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		etMessage.setText("");
	}

	private void refreshChatMessages() {
		//Log.d(LOG_TAG, "Refreshing chat messages ...");

		final FindCallback<ParseObject> findCallback = new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> poList, ParseException pex) {
				if (pex != null) {
					Log.e(LOG_TAG, "Failed to fetch chat messages. " + pex.getMessage());
					Toast.makeText(getApplicationContext(), pex.getMessage(), Toast.LENGTH_SHORT).show();
					return;
				}

				onFetchChatMessages(poList);
			}
		};

		final ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS_NAME);
		query.whereEqualTo("meta", PARSE_META_TOKEN);
		query.setSkip(messageSkipCount);
		query.findInBackground(findCallback);
	}

	private void onFetchChatMessages(List<ParseObject> poList) {
		for (ParseObject po : poList) {
			final ChatMessage cm = new ChatMessage(po.getObjectId(), po.getString("message"), po.getString("meta"));
			cm.setCreateAt(po.getCreatedAt());
			cm.setProfileColor(Utils.GetRandomColor());
			messagesAdapter.add(cm);
		}

		messageSkipCount += poList.size();
		messagesAdapter.notifyDataSetChanged();
	}
}
