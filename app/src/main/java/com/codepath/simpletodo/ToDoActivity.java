package com.codepath.simpletodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ToDoActivity extends ActionBarActivity implements IUndoHost {
	private static final String TODO_NODE_NAME   = "tasks";
	private static final String TODO_FILE_NAME   = "todo.json";
	private static final String TODO_NAME        = "name";
	private static final String TODO_CREATED     = "created";
	private static final String TODO_DEADLINE    = "deadline";
	private static final String TODO_ARCHIVED    = "archived";
	private static final String TODO_DESCRIPTION = "description";

	private static final String   CTX_MENU_ARCHIVE = "Archive";
	private static final String   CTX_MENU_DELETE  = "Delete";
	private static final String[] CTX_MENU_ITEMS   = new String[]{CTX_MENU_ARCHIVE, CTX_MENU_DELETE};

	private ListView                     todoList;
	private UndoBar                      undoBar;
	private ArrayAdapter<Map<String, Object>> itemsAdapter;
	private ArrayList<Map<String, Object>> items = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);

		todoList = (ListView) findViewById(R.id.todoList);
		undoBar = new UndoBar(this);

		getBaseContext();
		getApplicationContext();

		try {
			final ArrayList<Map<String, Object>> todoList = loadAllToDos();
			items.addAll(todoList);
		} catch (ParseException | IOException | JSONException ex) {
			ex.printStackTrace();
			finish();
		}

		initializeListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu,
	                                View v,
	                                ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() != R.id.todoList) {
			return;
		}

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		menu.setHeaderTitle("Choose"); //getResources().getStringArray(R.array.menu);
		for (int i = 0; i < CTX_MENU_ITEMS.length; i++) {
			menu.add(Menu.NONE, i, i, CTX_MENU_ITEMS[i]);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		//final String[] menuItems    = CTX_MENU_ITEMS; // getResources().getStringArray(R.array.menu);
		final int menuItemIndex     = item.getItemId();
		final String menuItemName   = CTX_MENU_ITEMS[menuItemIndex];
		final String listItemName   = items.get(info.position).get(TODO_NAME).toString();
		final StringBuilder sb      = new StringBuilder(listItemName);

		switch (menuItemName) {
			case CTX_MENU_ARCHIVE:
				sb.insert(0, "Archived ");
				Map<String, Object> todo = items.get(info.position);
				final Object value = Utils.ToAppDateFormat(null);
				todo.put(TODO_ARCHIVED, value);
				break;

			case CTX_MENU_DELETE:
				sb.insert(0, "Deleted ");
				items.remove(info.position);
				break;

			default:
				sb.insert(0, "Unknown action on ");
				break;
		}

		saveToDoList(items);
		itemsAdapter.notifyDataSetChanged();
		undoBar.show(sb.toString());

		return true;
	}

	public void onClickAddToDo(View view) {
		final EditText etAddItem = (EditText) findViewById(R.id.textAddItem);
		final String task = etAddItem.getText().toString().trim();
		if (Utils.IsNullOrEmpty(task)) { return; }
		final Map<String, Object> todo = createToDoItem(task, Utils.ToAppDateFormat(null), null, null, "");
		itemsAdapter.add(todo);
		etAddItem.setText("");

		saveToDoList(items);

		Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
	}

	private void initializeListView() {
		itemsAdapter = new ArrayAdapter<Map<String, Object>>(this,
				android.R.layout.simple_list_item_2,
				android.R.id.text1,
				items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);


				final Map<String, Object> todo  = items.get(position);
				final String name               = todo.get(TODO_NAME).toString();
				final String created            = todo.get(TODO_CREATED).toString();
				final String archived           = todo.get(TODO_ARCHIVED).toString();

				TextView text1                  = (TextView) view.findViewById(android.R.id.text1);
				TextView text2 = (TextView) view.findViewById(android.R.id.text2);

				text1.setText(name);
				text2.setText(created);

				if (archived.length() > 0) {
					text1.setTextColor(Color.GRAY);
					text2.setTextColor(Color.GRAY);
				}

				return view;
			}
		};

		todoList.setAdapter(itemsAdapter);

		registerForContextMenu(todoList);
	}

	private ArrayList<Map<String, Object>> loadAllToDos() throws ParseException, IOException, JSONException {
		final File file = ensureToDoFileExists();
		final ArrayList<Map<String, Object>> out = new ArrayList<>();

		final String content = FileUtils.readFileToString(file);
		final JSONObject json = new JSONObject(content);
		final JSONArray tasks = (JSONArray) json.get(TODO_NODE_NAME);

		for (int index = 0; index < tasks.length(); ++index) {
			final JSONObject jsonObject = tasks.getJSONObject(index);

			System.out.println("@" + index + ": " + jsonObject);

			final Map<String, Object> item = createToDoItem(jsonObject);
			out.add(item);
		}

		return out;
	}

	private File ensureToDoFileExists() throws IOException {
		final File file = new File(getFilesDir(), TODO_FILE_NAME);

		if (!file.exists()) {
			System.out.println("Creating first time tasks file ...");

			final boolean created = file.createNewFile();
			final String sample = loadSampleTasks();
			FileUtils.writeStringToFile(file, sample);
		}

		return file;
	}

	private void saveToDoList(ArrayList<Map<String, Object>> items) {
		if (items == null) {
			return;
		}

		final File file = new File(getFilesDir(), TODO_FILE_NAME);

		try {
			final JSONObject root = new JSONObject();
			root.put(TODO_NODE_NAME, new JSONArray());

			final JSONArray todos = root.getJSONArray(TODO_NODE_NAME);

			for (Map map : items) {
				final JSONObject json = new JSONObject(map);
				todos.put(json);
			}

			final String serialized = root.toString(2);
			FileUtils.writeStringToFile(file, serialized);
		} catch (IOException | JSONException ex) {
			ex.printStackTrace();

			final AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle("ToDo Application Error")
					.setMessage(ex.getMessage())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.create();

			dialog.show();
			finish();
		}
	}

	private String loadSampleTasks() throws IOException {
		final InputStream stream    = getResources().openRawResource(R.raw.sample);
		final byte[] data           = new byte[stream.available()];
		final int length            = stream.read(data);
		stream.close();
		return new String(data);
	}

	private HashMap<String, Object> createToDoItem(String name,
	                                          String created,
	                                          String deadline,
	                                          String archived,
	                                          String description) {
		final HashMap<String, Object> map = new LinkedHashMap<>();
		map.put(TODO_NAME, name);
		map.put(TODO_DESCRIPTION, description);
		map.put(TODO_CREATED, Utils.IsNullOrEmpty(created) ? Utils.ToAppDateFormat(null) : created);
		map.put(TODO_DEADLINE, deadline == null ? "" : deadline);
		map.put(TODO_ARCHIVED, archived == null ? "" : deadline);

		return map;
	}

    private HashMap<String, Object> createToDoItem(JSONObject item) throws ParseException {
        Object value = item.opt(TODO_NAME);
	    final String name = Utils.IsNullOrEmpty(value) ? "Unnamed" : value.toString();

	    value = item.opt(TODO_DESCRIPTION);
	    final String description = value == null ? "" : value.toString();

        value = item.opt(TODO_CREATED);
		// final Date created = Utils.IsNullOrEmpty(value) ? new Date() : Utils.ParseDate(value.toString());
	    final String created = value == null ? "" : value.toString();

        value = item.opt(TODO_DEADLINE);
	    final String deadline = value == null ? null : value.toString();

	    value = item.opt(TODO_ARCHIVED);
	    final String archived = value == null ? null : value.toString();

        return createToDoItem(name,
		        created,
		        deadline,
		        archived,
		        description);
    }

	@Override
	public Activity getHost() {
		return this;
	}

	@Override
	public void onClickedUndo(View view, Parcelable token) {
		undoBar.hide();
		Toast.makeText(this, "Undo action complete!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onUndoViewDismissed(View view) {
		Toast.makeText(this, "Dismissed!", Toast.LENGTH_LONG).show();
	}
}
