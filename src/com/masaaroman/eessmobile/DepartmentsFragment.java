package com.masaaroman.eessmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DepartmentsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myFragmentView = inflater.inflate(R.layout.fragment_departments, container, false);
		
		TextView tv = (TextView)myFragmentView.findViewById(R.id.depttext);
		tv.setText("This is the new text");
		
		return myFragmentView;
	}

}
