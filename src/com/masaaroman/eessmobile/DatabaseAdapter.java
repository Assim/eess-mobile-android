package com.masaaroman.eessmobile;

import java.util.ArrayList;

import com.masaaroman.eessmobile.model.Department;

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
}