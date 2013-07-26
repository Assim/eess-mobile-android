package com.masaaroman.eessmobile;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.masaaroman.eessmobile.model.DataJson;
import com.masaaroman.eessmobile.model.Department;
import com.masaaroman.eessmobile.model.DepartmentJson;
import com.masaaroman.eessmobile.model.Item;
import com.masaaroman.eessmobile.model.ItemJson;
import com.masaaroman.eessmobile.model.ProgressBarUpdater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAdapter extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eess";
    
    private static final String TABLE_DATA = "data";
    
    private static final String KEY_DATA_NAME = "name";
    private static final String KEY_DATA_VALUE = "value";
    
    private static final String TABLE_DEPARTMENTS = "departments";
    
    private static final String KEY_DEPARTMENTS_DEPARTMENT_ID = "department_id";
    private static final String KEY_DEPARTMENTS_NAME = "name";

    private static final String TABLE_ITEMS = "items";
    
    private static final String KEY_ITEMS_ITEM_ID = "item_id";
    private static final String KEY_ITEMS_DEPARTMENT_ID = "department_id";
    private static final String KEY_ITEMS_BARCODE = "barcode";
    private static final String KEY_ITEMS_NAME = "name";
    private static final String KEY_ITEMS_PRICE = "price";
    	
	public DatabaseAdapter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA + "(" + KEY_DATA_NAME + " TEXT," + KEY_DATA_VALUE + " TEXT" + ")";
		db.execSQL(CREATE_DATA_TABLE);
		
        String CREATE_DEPARTMENTS_TABLE = "CREATE TABLE " + TABLE_DEPARTMENTS + "(" + KEY_DEPARTMENTS_DEPARTMENT_ID + " INTEGER," + KEY_DEPARTMENTS_NAME + " TEXT" + ")";
        db.execSQL(CREATE_DEPARTMENTS_TABLE);
        
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "(" + KEY_ITEMS_ITEM_ID + " INTEGER," + KEY_ITEMS_DEPARTMENT_ID + " INTEGER, " + KEY_ITEMS_BARCODE + " INTEGER, " + KEY_ITEMS_NAME + " TEXT, " + KEY_ITEMS_PRICE + " TEXT " + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
 
        // Create tables again
        onCreate(db);
	}
	
	public void clearDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("TRUNCATE TABLE "+ TABLE_DATA);
		db.execSQL("TRUNCATE TABLE "+ TABLE_DEPARTMENTS);
		db.execSQL("TRUNCATE TABLE "+ TABLE_ITEMS);
	}
	
	public Department getDepartment(int departmentId) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_DEPARTMENTS, new String[] { KEY_DEPARTMENTS_DEPARTMENT_ID, KEY_DEPARTMENTS_NAME }, KEY_DEPARTMENTS_DEPARTMENT_ID + "=?", new String[] { String.valueOf(departmentId) }, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
		}
		
		Department department = new Department(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
		
		return department;
	}
	
	public ArrayList<Department> getAllDepartments() {
		ArrayList<Department> departmentList = new ArrayList<Department>();
		String selectQuery = "SELECT  * FROM " + TABLE_DEPARTMENTS;
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);		
		if(cursor.moveToFirst()) {
			do {
				departmentList.add(new Department(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
			} while(cursor.moveToNext());
		}
		
		return departmentList;
	}
	
	public void addDepartments(DepartmentJson data, ProgressBarUpdater progressBarUpdater) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		for(int i=0; i<data.size(); i++) {
			ContentValues values = new ContentValues();
			values.put(KEY_DEPARTMENTS_DEPARTMENT_ID, data.get(i).getDepartmentId());
			values.put(KEY_DEPARTMENTS_NAME, data.get(i).getName());
			db.insert(TABLE_DEPARTMENTS, null, values);
			progressBarUpdater.increment();
		}
		
		db.close();
	}
	
	public Item getItem(int itemId) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_ITEMS_ITEM_ID, KEY_ITEMS_DEPARTMENT_ID, KEY_ITEMS_BARCODE, KEY_ITEMS_NAME, KEY_ITEMS_PRICE }, KEY_ITEMS_ITEM_ID + "=?", new String[] { String.valueOf(itemId) }, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
		}
		
		Item item = new Item(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), cursor.getString(3), new BigDecimal(cursor.getString(4)));
		
		return item;
	}
	
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> itemList = new ArrayList<Item>();
		String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()) {
			do {
				itemList.add(new Item(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), cursor.getString(3), new BigDecimal(cursor.getString(4))));
			} while(cursor.moveToNext());
		}
		
		return itemList;
	}
	
	public void addItems(ItemJson data, ProgressBarUpdater progressBarUpdater) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		for(int i=0; i<data.size(); i++) {
			ContentValues values = new ContentValues();
			values.put(KEY_ITEMS_ITEM_ID, data.get(i).getItemId());
			values.put(KEY_ITEMS_DEPARTMENT_ID, data.get(i).getDepartmentId());
			values.put(KEY_ITEMS_BARCODE, data.get(i).getBarcode());
			values.put(KEY_ITEMS_NAME, data.get(i).getName());
			values.put(KEY_ITEMS_PRICE, data.get(i).getPrice().toString());
			db.insert(TABLE_ITEMS, null, values);
			progressBarUpdater.increment();
		}
		
		db.close();
	}
	
	public void addData(DataJson data) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		for(int i=0; i<data.size(); i++) {
			ContentValues values = new ContentValues();
			values.put(KEY_DATA_NAME, data.get(i).getName());
			values.put(KEY_DATA_VALUE, data.get(i).getValue());
			db.insert(TABLE_DATA, null, values);
		}
		
		db.close();
	}
	
	public long getLastUpdate() {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_DATA, new String[] { KEY_DATA_VALUE }, KEY_DATA_NAME + "=?", new String[] { "last_update" }, null, null, null);
		if(cursor != null) {
			if(cursor.moveToFirst()) {
				long result = Long.parseLong(cursor.getString(0));
				return result;
			}
		}
		return 0;
	}
}