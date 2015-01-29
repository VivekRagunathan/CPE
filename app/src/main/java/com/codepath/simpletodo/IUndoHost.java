package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.view.View;

public interface IUndoHost {
	Activity getHost();
	void onClickedUndo(View view, Parcelable token);
	void onUndoViewDismissed(View view);
}
