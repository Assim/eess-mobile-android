package com.masaaroman.eessmobile;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.masaaroman.eessmobile.model.Cart;
import com.masaaroman.eessmobile.model.CartItem;
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
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "eess";
    
	public static final String TABLE_DATA = "data";
    
	public static final String KEY_DATA_DATA_ID = "_id";
	public static final String KEY_DATA_NAME = "name";
	public static final String KEY_DATA_VALUE = "value";
    
	public static final String TABLE_DEPARTMENTS = "departments";
    
	public static final String KEY_DEPARTMENTS_DEPARTMENT_ID = "_id";
	public static final String KEY_DEPARTMENTS_NAME = "name";

	public static final String TABLE_ITEMS = "items";
    
	public static final String KEY_ITEMS_ITEM_ID = "_id";
	public static final String KEY_ITEMS_DEPARTMENT_ID = "department_id";
	public static final String KEY_ITEMS_BARCODE = "barcode";
	public static final String KEY_ITEMS_NAME = "name";
	public static final String KEY_ITEMS_PRICE = "price";
	public static final String KEY_ITEMS_PICTURE = "picture";
	
	public static final String TABLE_CART = "cart";
	
	public static final String KEY_CART_ID = "_id";
	public static final String KEY_CART_ITEM_ID = "item_id";
	public static final String KEY_CART_UNIT_PRICE = "unit_price";
	public static final String KEY_CART_QTY = "qty";
    	
	public DatabaseAdapter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA + "(" + KEY_DATA_DATA_ID + " INTEGER PRIMARY KEY," + KEY_DATA_NAME + " TEXT," + KEY_DATA_VALUE + " TEXT" + ")";
		db.execSQL(CREATE_DATA_TABLE);
		
        String CREATE_DEPARTMENTS_TABLE = "CREATE TABLE " + TABLE_DEPARTMENTS + "(" + KEY_DEPARTMENTS_DEPARTMENT_ID + " INTEGER PRIMARY KEY," + KEY_DEPARTMENTS_NAME + " TEXT" + ")";
        db.execSQL(CREATE_DEPARTMENTS_TABLE);
        
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "(" + KEY_ITEMS_ITEM_ID + " INTEGER PRIMARY KEY," + KEY_ITEMS_DEPARTMENT_ID + " INTEGER, " + KEY_ITEMS_BARCODE + " INTEGER, " + KEY_ITEMS_NAME + " TEXT, " + KEY_ITEMS_PRICE + " TEXT, " + KEY_ITEMS_PICTURE + " TEXT" + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
        
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "(" + KEY_CART_ID + " INTEGER PRIMARY KEY," + KEY_CART_ITEM_ID + " INTEGER, " + KEY_CART_UNIT_PRICE + " TEXT, " + KEY_CART_QTY + " INTEGER" + ")";
        db.execSQL(CREATE_CART_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
 
        // Create tables again
        onCreate(db);
	}
	
	public void clearDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, null, null);
		db.delete(TABLE_DEPARTMENTS, null, null);
		db.delete(TABLE_ITEMS, null, null);
		db.delete(TABLE_CART, null, null);
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
	
	public Cursor getAllDepartmentsCursor() {
		String selectQuery = "SELECT  * FROM " + TABLE_DEPARTMENTS;
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		
		return cursor;
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
		
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_ITEMS_ITEM_ID, KEY_ITEMS_DEPARTMENT_ID, KEY_ITEMS_BARCODE, KEY_ITEMS_NAME, KEY_ITEMS_PRICE, KEY_ITEMS_PICTURE }, KEY_ITEMS_ITEM_ID + "=?", new String[] { String.valueOf(itemId) }, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
		}
		
		Item item = new Item(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), cursor.getString(3), new BigDecimal(cursor.getString(4)), cursor.getString(5));
		
		return item;
	}
	
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> itemList = new ArrayList<Item>();
		String selectQuery = "SELECT * FROM " + TABLE_ITEMS;
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()) {
			do {
				itemList.add(new Item(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), cursor.getString(3), new BigDecimal(cursor.getString(4)), cursor.getString(5)));
			} while(cursor.moveToNext());
		}
		
		return itemList;
	}
	
	public Cursor getAllItemsCursor() {
		String selectQuery = "SELECT * FROM " + TABLE_ITEMS;
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		
		return cursor;
	}
	
	public ArrayList<Item> getDepartmentItems(int departmentId) {
		ArrayList<Item> itemList = new ArrayList<Item>();
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_ITEMS_ITEM_ID, KEY_ITEMS_DEPARTMENT_ID, KEY_ITEMS_BARCODE, KEY_ITEMS_NAME, KEY_ITEMS_PRICE, KEY_ITEMS_PICTURE }, KEY_ITEMS_DEPARTMENT_ID + "=?", new String[] { String.valueOf(departmentId) }, null, null, null);
		if(cursor.moveToFirst()) {
			do {
				itemList.add(new Item(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), cursor.getString(3), new BigDecimal(cursor.getString(4)), cursor.getString(5)));
			} while(cursor.moveToNext());
		}
		return itemList;
	}
	
	public Cursor getDepartmentItemsCursor(int departmentId) {		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_ITEMS_ITEM_ID, KEY_ITEMS_DEPARTMENT_ID, KEY_ITEMS_BARCODE, KEY_ITEMS_NAME, KEY_ITEMS_PRICE, KEY_ITEMS_PICTURE }, KEY_ITEMS_DEPARTMENT_ID + "=?", new String[] { String.valueOf(departmentId) }, null, null, null);
		
		return cursor;
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
			values.put(KEY_ITEMS_PICTURE, data.get(i).getPicture());
			db.insert(TABLE_ITEMS, null, values);
			progressBarUpdater.increment();
		}
		
		db.close();
	}
	
	public Cursor searchItems(String search) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_ITEMS_ITEM_ID, KEY_ITEMS_DEPARTMENT_ID, KEY_ITEMS_BARCODE, KEY_ITEMS_NAME, KEY_ITEMS_PRICE, KEY_ITEMS_PICTURE }, KEY_ITEMS_NAME + "LIKE '%" + search + "%'", null, null, null, null);
		
		return cursor;
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
	
	public void clearCart() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CART, null, null);
	}
	
	public Cart getCart() {
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		String selectQuery = "SELECT * FROM " + TABLE_CART;
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()) {
			do {
				items.add(new CartItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), new BigDecimal(cursor.getString(2)), Integer.parseInt(cursor.getString(3))));
			} while(cursor.moveToNext());
		}
				
		// TODO fix total price issue
		Cart cart = new Cart();
		cart.setItemsArrayList(items);
		
		return cart;
	}
}