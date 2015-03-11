package com.codepath.simplechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatMessagesAdapter extends ArrayAdapter<ChatMessage> {

    public ChatMessagesAdapter(Context context, ArrayList<ChatMessage> data) {
        super(context, R.layout.item_message, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        ImageView ivUserImage   = (ImageView)convertView.findViewById(R.id.ivUserImage);
        TextView tvMessage      = (TextView)convertView.findViewById(R.id.tvMessage);
        TextView tvWhen         = (TextView)convertView.findViewById(R.id.tvWhen);

	    ivUserImage.setBackgroundColor(item.getProfileColor());
        tvMessage.setText(item.getMessage());
        tvWhen.setText(item.getCreateAt().toString());

        return convertView;
    }
}
