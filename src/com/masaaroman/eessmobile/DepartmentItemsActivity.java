package com.masaaroman.eessmobile;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DepartmentItemsActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_items);
		
		int departmentId = (int) getIntent().getExtras().getLong("department_id");
		String departmentName = getIntent().getExtras().getString("department_name");
		
		// Get items
		DatabaseAdapter db = new DatabaseAdapter(this);
		Cursor cursor = db.getDepartmentItemsCursor(departmentId);
		
		TextView tv = (TextView)findViewById(R.id.departmentName);
		tv.setText("Department: "+departmentName);
		
		String[] from = {
			DatabaseAdapter.KEY_ITEMS_NAME
		};
		
		int[] to = {
			R.id.itemName
		};
		
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_department_items_row, cursor, from, to);
		
		ListView lv = (ListView)findViewById(R.id.itemsList);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
			}
		});
	}
}