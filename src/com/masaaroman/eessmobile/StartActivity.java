package com.masaaroman.eessmobile;

import com.google.gson.Gson;
import com.masaaroman.eessmobile.model.DataJson;
import com.masaaroman.eessmobile.model.DepartmentJson;
import com.masaaroman.eessmobile.model.ItemJson;
import com.masaaroman.eessmobile.model.ProgressBarUpdater;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
		
		new FetchDataTask().execute(dataTask, departmentTask, itemTask);
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
					 } while(json.equals(""));
					 
					 publishProgress(0);
					 
					 // TODO check for updates
					 // TODO add last update to internal database
					 DataJson data = gson.fromJson(json, DataJson.class);
					 for(int j=0; j<data.size(); j++) {
						 String name = data.get(j).getName();
						 String value = data.get(j).getValue();
						 
						 if(name.equals("departments_count") || name.equals("items_counts")) {
							 itemsToComplete += Integer.parseInt(value);
						 }
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
					 } while(json.equals(""));
					 
					 publishProgress(0);
					 
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
					 } while(json.equals(""));
					 
					 publishProgress(0);
					 
					 ItemJson itemData = gson.fromJson(json, ItemJson.class);
					 db.addItems(itemData, progressBarUpdater);
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
	    	 
	    	 // Show a reconnecting message just in case data was not fetched
	    	 if(progress[0] == -1) {
	    		 Toast.makeText(getApplicationContext(), "Connection error. Reconnecting...", Toast.LENGTH_SHORT).show();
	    	 }
	     }
		
		@Override
		protected void onPostExecute(Long result) {
	         Toast.makeText(getApplicationContext(), "Application loaded.", Toast.LENGTH_LONG).show();
		}
	}
}