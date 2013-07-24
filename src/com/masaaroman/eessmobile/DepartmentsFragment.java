package com.masaaroman.eessmobile;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DepartmentsFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myFragmentView = inflater.inflate(R.layout.fragment_departments, container, false);
		String[] values = new String[] { "Canned Food", "Frozen Food", "Bakery", "Meats", "Cleaning Items" };

		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
			list.add(values[i]);
		}
			    
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);

		return myFragmentView;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    
	}
}
