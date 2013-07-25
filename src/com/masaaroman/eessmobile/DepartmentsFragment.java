package com.masaaroman.eessmobile;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.masaaroman.eessmobile.model.DepartmentJson;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DepartmentsFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myFragmentView = inflater.inflate(R.layout.fragment_departments, container, false);
		
		// To prevent android.os.NetworkOnMainThreadException
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		Gson gson = new Gson();
		ArrayList<String> list = new ArrayList<String>();
		String json = "";
		try {
			json = Utilities.readUrl("http://masaaroman.com/interface/index.php/departments/");
		} catch (Exception e) {
			Toast.makeText(myFragmentView.getContext(), e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		if(json.equals("")) {
			Toast.makeText(myFragmentView.getContext(), "Could not get data from server. There might be a problem with the internet connection.", Toast.LENGTH_LONG).show();
		}
		else {
			DepartmentJson deptData = gson.fromJson(json, DepartmentJson.class);
			
			for (int i = 0; i < deptData.size(); i++) {
				list.add(deptData.get(i).getName());
			}
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);

		return myFragmentView;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    
	}
}