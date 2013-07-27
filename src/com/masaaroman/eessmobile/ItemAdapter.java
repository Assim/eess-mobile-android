package com.masaaroman.eessmobile;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class ItemAdapter extends SimpleCursorAdapter {

	public ItemAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}
}