package com.codepath.simpletodo;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.github.johnpersano.supertoasts.util.OnDismissWrapper;
import com.github.johnpersano.supertoasts.util.Style;

public class UndoBar {
	private static final String TAG = "todo_undo";

	private SuperActivityToast superActivityToast;
	private IUndoHost parent;

	public UndoBar(IUndoHost host) {
		this.parent = host;

		superActivityToast = new SuperActivityToast(host.getHost(), SuperToast.Type.BUTTON);
		superActivityToast.setDuration(SuperToast.Duration.MEDIUM);
		superActivityToast.setButtonIcon(SuperToast.Icon.Dark.UNDO, "UNDO");

		/**
		 * The OnClickWrapper is needed to reattach SuperToast.OnClickListeners on orientation changes.
		 * It does this via a unique String tag defined in the first parameter so each OnClickWrapper's tag
		 * should be unique.
		 */
		final OnClickWrapper clickWrapper = new OnClickWrapper(TAG, new SuperToast.OnClickListener() {
			@Override
			public void onClick(View view, Parcelable token) {
				final Context context = parent.getHost().getApplicationContext();

				/*Toast.makeText(context,
						"[" + parent.getHost().getTitle() + "] Action Undone!",
						Toast.LENGTH_SHORT).show();*/

				parent.onClickedUndo(view, token);
			}
		});

		final OnDismissWrapper dismissWrapper = new OnDismissWrapper(TAG, new SuperToast.OnDismissListener() {
			@Override public void onDismiss(View view) {
				final Context context = parent.getHost().getApplicationContext();

				/*Toast.makeText(context,
						"[" + parent.getHost().getTitle() + "] Dismissed!",
						Toast.LENGTH_SHORT).show();*/

				parent.onUndoViewDismissed(view);
			}
		});

		superActivityToast.setOnClickWrapper(clickWrapper);
		superActivityToast.setOnDismissWrapper(dismissWrapper);
	}

	public void show(String message) {
		superActivityToast.setText(message);
		superActivityToast.show();
	}

	public void hide() {
		superActivityToast.dismiss();
	}
}
