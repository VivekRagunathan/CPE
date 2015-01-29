package com.codepath.simpletodo;

import android.app.Activity;
import android.os.Parcelable;
import android.view.View;

public abstract class AbstractUndoHost implements IUndoHost {
	@Override public abstract Activity getHost();

	@Override
	public void onClickedUndo(View view, Parcelable token) {}

	@Override
	public void onUndoViewDismissed(View view) {}
}
