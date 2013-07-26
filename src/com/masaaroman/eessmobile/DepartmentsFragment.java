package com.masaaroman.eessmobile;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DepartmentsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View myFragmentView = inflater.inflate(R.layout.fragment_departments, container, false);
		
		// To prevent android.os.NetworkOnMainThreadException
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		ArrayList<String> list = new ArrayList<String>();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, list);
		ListView lv = (ListView)myFragmentView.findViewById(R.id.deptList);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
			}
		});

		return myFragmentView;
	}
}