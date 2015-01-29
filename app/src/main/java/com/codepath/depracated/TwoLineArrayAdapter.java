package com.codepath.simpletodo.depracated;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public abstract class TwoLineArrayAdapter<T> extends ArrayAdapter<T> {
    private int listItemResId;

    public TwoLineArrayAdapter(Context context, T[] items) {
        this(context, android.R.layout.two_line_list_item, items);
    }

    public TwoLineArrayAdapter(
            Context context,
            int listItemLayoutResourceId,
            T[] ts) {
        super(context, listItemLayoutResourceId, ts);
        listItemResId = listItemLayoutResourceId;
    }

    @Override
    public android.view.View getView(
            int position,
            View convertView,
            ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItemView = convertView;
        if (null == convertView) {
            listItemView = inflater.inflate(
                    listItemResId,
                    parent,
                    false);
        }

        TextView lineOneView = (TextView)listItemView.findViewById(android.R.id.text1);
        TextView lineTwoView = (TextView)listItemView.findViewById(android.R.id.text2);

        T t = (T) getItem(position);
        lineOneView.setText(lineOneText(t));
        lineTwoView.setText(lineTwoText(t));

        return listItemView;
    }

    public abstract String lineOneText(T t);

    public abstract String lineTwoText(T t);
}
