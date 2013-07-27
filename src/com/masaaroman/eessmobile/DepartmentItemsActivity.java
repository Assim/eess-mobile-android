package com.masaaroman.eessmobile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DepartmentItemsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_items);
		
		int departmentId = (int) getIntent().getExtras().getLong("department_id");
		String departmentName = getIntent().getExtras().getString("department_name");
		
		// Get items
		final DatabaseAdapter db = new DatabaseAdapter(this);
		Cursor cursor = db.getDepartmentItemsCursor(departmentId);
		
		TextView tv = (TextView)findViewById(R.id.departmentName);
		tv.setText("Department: "+departmentName);
		
		String[] from = {
			DatabaseAdapter.KEY_ITEMS_NAME
		};
		
		int[] to = {
			R.id.itemName
		};
		
		ItemAdapter adapter = new ItemAdapter(this, R.layout.row_item, cursor, from, to, 0);
		
		ListView lv = (ListView)findViewById(R.id.itemsList);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				// Click event
			}
		});
		
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(!db.getItem((int)id).getPicture().equals("")) {
					Intent i = new Intent(getApplicationContext(), ItemPictureActivity.class);
					Bundle bundle = new Bundle();
					bundle.putLong("item_id", id);
					i.putExtras(bundle);
					startActivity(i);
				}
				else {
					Toast.makeText(getApplicationContext(), "This item has no picture.", Toast.LENGTH_SHORT).show();
				}
				return true; // Just so that it doesn't call the onItemClick
			}
		});
	}
}