package com.masaaroman.eessmobile;

import com.google.gson.Gson;
import com.masaaroman.eessmobile.model.DepartmentJson;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {
	
	private DatabaseAdapter db;
	private String api;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_start);
		setProgressBarIndeterminateVisibility(true);
		
		TextView statusText = (TextView)findViewById(R.id.statusText);
		statusText.setText("Checking for updates...");
		
		api = "http://masaaroman.com/api/index.php/";
		db = new DatabaseAdapter(this);
		
		TaskObject task = new TaskObject(api+"departments", "departments");
		
		new FetchDataTask().execute(task);
	}
	
	private class TaskObject {
		private String url;
		private String table;
		
		public TaskObject(String url, String table) {
			this.url = url;
			this.table = table;
		}
		
		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getUrl() {
			return this.url;
		}
		
		public void setTable(String table) {
			this.table = table;
		}
		
		public String getTable() {
			return this.table;
		}
	}
	
	private class FetchDataTask extends AsyncTask<TaskObject, Integer, Long> {

		@Override
		protected Long doInBackground(TaskObject... tasks) {
			 Gson gson = new Gson();
			 String json = "";
			 boolean firstTry = true;
			 
			 // Loop for the tasks
			 for (int i = 0; i < tasks.length; i++) {
				 if(tasks[i].getTable().equals("departments")) {
					 // Get JSON from API
					 do {
						 if(!firstTry) {
							 publishProgress(-1);
						 }
						 
						 try {
							 json = Utilities.readUrl(tasks[i].getUrl());
						 } catch (Exception e) {
							 e.printStackTrace();
						 }
					 } while(json.equals(""));
					 
					 publishProgress(0);
					 
					 DepartmentJson deptData = gson.fromJson(json, DepartmentJson.class);
					 db.addDepartments(deptData);
				 }
				 else if(tasks[i].getTable().equals("items")) {
					 
				 }
				 else if(tasks[i].getTable().equals("data")) {
					 
				 }
				 
				 // So the next task is considered in it's first try
				 firstTry = true;
			 }
			 return null;
		}
		
	     protected void onProgressUpdate(Integer... progress) {
	    	 if(progress[0] == -1) {
		         Toast.makeText(getApplicationContext(), "Retrying...", Toast.LENGTH_SHORT).show();
	    		 
	    	 }
	    	 else {

		         Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
	    	 }
	     }
		
		@Override
		protected void onPostExecute(Long result) {
	         Toast.makeText(getApplicationContext(), "works", Toast.LENGTH_LONG).show();
		}
	}
}