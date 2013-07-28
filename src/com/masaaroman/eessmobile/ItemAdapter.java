package com.masaaroman.eessmobile;

import com.masaaroman.eessmobile.model.Cart;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ItemAdapter extends SimpleCursorAdapter {

	private Cart cart;
	
	public ItemAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Cart cart) {
		super(context, layout, c, from, to, flags);
		setCart(cart);
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	public Cart getCart() {
		return this.cart;
	}
	
	public void updateItemsBasedOnCart() {
		for(int i = 0; i<getCount(); i++) {
			View v = getView(i, null, null);
			TextView tv = (TextView)v.findViewById(R.id.itemName);
			tv.setText("ban");
		}
	}
}