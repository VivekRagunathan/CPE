package com.codepath.simpletodo.depracated;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

public class ToDoItemArrayAdapter extends TwoLineArrayAdapter<Map<String, ?>> {
	private static Map<String, ?>[] TYPE;

    public ToDoItemArrayAdapter(Context context, ArrayList<Map<String, ?>> items) {
        super(context, items.toArray(TYPE));
    }

    @Override
    public String lineOneText(Map<String, ?> map) {
        return map.get("name").toString();
    }

    @Override
    public String lineTwoText(Map<String, ?> map) {
        return map.get("created").toString();
    }
}
