package com.codepath.ipix;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.ipix.ipix.R;

public class CommentsDialog extends DialogFragment {

	private CommentsAdapter commentsAdapter;

	public CommentsDialog() {
	}

	public static CommentsDialog newInstance(InstagramMedia.Comment[] comments) {
		final String[] texts = new String[comments.length];
		for (int index = 0; index < comments.length; ++index) {
			texts[index] = comments[index].text();
		}

		Bundle args = new Bundle();
		args.putStringArray("comments", texts);

		final CommentsDialog fragment = new CommentsDialog();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.comments, container);
		final ListView lvComments = (ListView) view.findViewById(R.id.lvComments);
		lvComments.setAdapter(commentsAdapter);

		getDialog().setTitle("Comments");

		/*mEditText = (EditText) view.findViewById(R.id.txt_your_name);
		String title = getArguments().getString("title", "Enter Name");

		// Show soft keyboard automatically
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);*/

		return view;
	}
}
