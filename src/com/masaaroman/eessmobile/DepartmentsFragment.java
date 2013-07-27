package com.masaaroman.eessmobile;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class DepartmentsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View myFragmentView = inflater.inflate(R.layout.fragment_departments, container, false);
		
		// To prevent android.os.NetworkOnMainThreadException
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		DatabaseAdapter db = new DatabaseAdapter(myFragmentView.getContext());
		
		// Get departments
		Cursor cursor = db.getAllDepartmentsCursor();
		
		String[] from = {
			DatabaseAdapter.KEY_DEPARTMENTS_NAME	
		};
		
		int[] to = {
			R.id.departmentName	
		};
		
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(myFragmentView.getContext(), R.layout.fragment_departments_row, cursor, from, to);
		
		ListView lv = (ListView)myFragmentView.findViewById(R.id.deptList);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				Cursor c = (Cursor)parent.getAdapter().getItem(position);
				
				Intent i = new Intent(getActivity().getApplicationContext(), DepartmentItemsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("department_name", c.getString(1));
				bundle.putLong("department_id", id);
				i.putExtras(bundle);
				startActivity(i);				
			}
		});

		return myFragmentView;
	}
}