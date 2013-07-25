package com.masaaroman.eessmobile;

import java.util.ArrayList;

import android.content.Context;
import android.util.SparseArray;
import android.widget.ArrayAdapter;

public class DepartmentAdapter extends ArrayAdapter<String> {

	SparseArray<String> elements = new SparseArray<String>();
	
	public DepartmentAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		for (int i = 0; i < objects.size(); ++i) {
			elements.put(i, objects.get(i));
		}
	}
}
