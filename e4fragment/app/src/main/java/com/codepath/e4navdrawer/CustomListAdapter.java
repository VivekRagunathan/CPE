package com.codepath.e4navdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class CustomListAdapter extends BaseAdapter {
	private ArrayList<String> listData;
	private LayoutInflater    layoutInflater;

	public CustomListAdapter(Context context, ArrayList<String> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.drawer_nav_item, null);
			holder = new ViewHolder();
			/*holder.headlineView = (TextView) convertView.findViewById(R.id.text1);
			holder.reporterNameView = (TextView) convertView.findViewById(R.id.reporter);
			holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);*/
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*holder.headlineView.setText(((String)listData.get(position)).getHeadline());
		holder.reporterNameView.setText("By, " + ((String)listData.get(position)).getReporterName());
		holder.reportedDateView.setText(((String)listData.get(position)).getDate());*/

		return convertView;
	}

	static class ViewHolder {
		TextView headlineView;
		TextView reporterNameView;
		TextView reportedDateView;
	}

	public void add(String String) {
		listData.add(String);
	}
}
