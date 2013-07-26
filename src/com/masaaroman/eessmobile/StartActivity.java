package com.masaaroman.eessmobile;

import com.google.gson.Gson;
import com.masaaroman.eessmobile.model.DataJson;
import com.masaaroman.eessmobile.model.DepartmentJson;
import com.masaaroman.eessmobile.model.ItemJson;
import com.masaaroman.eessmobile.model.ProgressBarUpdater;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StartActivity extends Activity {
	
	private DatabaseAdapter db;
	private String api;
	private int itemsToComplete;
	private TextView statusText;
	private ProgressBar progressBar;
	private ProgressBarUpdater progressBarUpdater;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_start);
		setProgressBarIndeterminateVisibility(true);
		
		statusText = (TextView)findViewById(R.id.statusText);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		
		statusText.setText("Checking for updates...");
		
		api = "http://masaaroman.com/api/index.php/";
		db = new DatabaseAdapter(this);
		
		TaskObject dataTask = new TaskObject(api, "data");
		TaskObject departmentTask = new TaskObject(api+"departments", "departments");
		TaskObject itemTask = new TaskObject(api+"items", "items");
		
		// Make sure data is always first because the other work is dependant on it.
		new FetchDataTask().execute(dataTask, departmentTask, itemTask);
	}
	
	private void startMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
		// So that this activity can't be accessed from the back button
		finish();
	}
	
	private class TaskObject {
		private String url;
		private String table;
		
		public TaskObject(String url, String table) {
			this.url = url;
			this.table = table;
		}
		
		public String getUrl() {
			return this.url;
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
				 if(!isCancelled()) {
				 if(tasks[i].getTable().equals("data")) {
					 do {
						 if(!firstTry) {
							 publishProgress(-1);
						 }
						 
						 try {
							 json = Utilities.readUrl(tasks[i].getUrl());
						 } catch (Exception e) {
							 e.printStackTrace();
						 }
						 // If it repeats, it won't be the first try
						 firstTry = false;
					 } while(json.equals(""));
					 
					 publishProgress(1);
					 
					 DataJson data = gson.fromJson(json, DataJson.class);
					 long lastUpdate = 0; // will be set below
					 for(int j=0; j<data.size(); j++) {
						 String name = data.get(j).getName();
						 String value = data.get(j).getValue();
						 
						 if(name.equals("departments_count") || name.equals("items_counts")) {
							 itemsToComplete += Integer.parseInt(value);
						 }
						 else if(name.equals("last_update")) {
							 lastUpdate = Integer.parseInt(value);
						 }
					 }
					 
					 // Check if API last update is bigger than last update in app
					 // Then it requires an update, otherwise cancel
					 if(lastUpdate > db.getLastUpdate()) {
						 // Add "data" table to database and continue adding the rest
						 db.addData(data);
					 }
					 else {
						 // End task because all data is downloaded
						 cancel(true);
					 }
					 
					 // So that it'll make a new object of ProgressBarUpdater
					 publishProgress(1);
				 }
				 else if(tasks[i].getTable().equals("departments")) {
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
						 // If it repeats, it won't be the first try
						 firstTry = false;
					 } while(json.equals(""));
					 
					 publishProgress(2);
					 
					 DepartmentJson deptData = gson.fromJson(json, DepartmentJson.class);
					 db.addDepartments(deptData, progressBarUpdater);
				 }
				 else if(tasks[i].getTable().equals("items")) {
					 do {
						 if(!firstTry) {
							 publishProgress(-1);
						 }
						 
						 try {
							 json = Utilities.readUrl(tasks[i].getUrl());
						 } catch (Exception e) {
							 e.printStackTrace();
						 }
						 // If it repeats, it won't be the first try
						 firstTry = false;
					 } while(json.equals(""));
					 
					 publishProgress(2);
					 
					 ItemJson itemData = gson.fromJson(json, ItemJson.class);
					 db.addItems(itemData, progressBarUpdater);
				 }
				 }
				 // So the next task is considered in it's first try
				 firstTry = true;
			 }
			 return null;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			// This means data is loaded, and make a ProgressBarUpdater
			if(progress[0] == 1) {
			         // Make a new object of progress bar updater
		    		 progressBarUpdater = new ProgressBarUpdater(progressBar, itemsToComplete);
			}
		    	 
			// This means there is a connection error
			if(progress[0] == -1) {
				statusText.setText("Connection error. Trying to reconnect...");
			}
		    	 
			if(progress[0] == 2) {
					statusText.setText("Downloading data...");
			}
		}
		
		protected void onCancelled() {
			onPostExecute(0L);
		}
			
		@Override
		protected void onPostExecute(Long result) {
			statusText.setText("All data downloaded. Starting...");
			setProgressBarIndeterminateVisibility(false);
			startMainActivity();
		}
	}
}