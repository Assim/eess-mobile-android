package com.masaaroman.eessmobile.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cart {
	private static Cart instance;
	
	private ArrayList<CartItem> items;
	private BigDecimal amount;
	private String phoneNumber;
	
	private Cart() {} // For using Singleton pattern
	
	public Cart getInstance() {
		if(instance == null)
			instance = new Cart();
		return instance;
	}
	
	public void addItem(Item item) {
		// Since we are adding an item either way, lets add to the amount
		setAmount(getAmount().add(item.getPrice()));
		
		int itemId = item.getItemId();
		for(int i=0; i<items.size(); i++) {
			// Check if it exists in the cart
			if(itemId == items.get(i).getId()) {
				// Add one to quantity
				items.get(i).setQty(items.get(i).getQty() + 1);
				return;
			}
		}
		
		// Otherwise it doesn't exist, so add one
		CartItem cartItem = new CartItem();
		cartItem.setId(item.getItemId());
		cartItem.setName(item.getName());
		cartItem.setQty(1);
		cartItem.setUnitPrice(item.getPrice());
		
		items.add(cartItem);
	}
	
	public void decreaseItem(Item item) {
		// Since we are decreasing an item either way, lets deduct from the amount
		setAmount(getAmount().subtract(item.getPrice()));
		
		int itemId = item.getItemId();
		for(int i=0; i<items.size(); i++) {
			// Find item in cart
			if(itemId == items.get(i).getId()) {
				CartItem selectedItem = items.get(i); // for performance
				
				// If more than one item
				if(selectedItem.getQty() > 1) {
					// Just reduce quantity
					selectedItem.setQty(selectedItem.getQty() - 1);
				}
				else if(selectedItem.getQty() == 1) {
					// Remove object from cart because last item
					items.remove(i);
				}
				
				// If you reached here, means the job is done
				break;
			}
		}
	}
	
	public void removeItem(Item item) {
		int itemId = item.getItemId();
		for(int i=0; i<items.size(); i++) {
			if(itemId == items.get(i).getId()) {
				CartItem selectedItem = items.get(i);
				
				// Subtract from amount based on qty removed
				BigDecimal toBeDeducted = new BigDecimal(selectedItem.getQty()).multiply(selectedItem.getUnitPrice());
				setAmount(getAmount().subtract(toBeDeducted));
				
				// Remove from cart
				items.remove(i);
				
				// Job is done
				break;
			}
		}
	}
	
	public void clearCart() {
		items.clear();
		setAmount(new BigDecimal(0));
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	public boolean setPhoneNumber(String phoneNumber) {
		Pattern pattern = Pattern.compile("9\\d{7}"); // Starts with 9 and has 7 digits after it.
		Matcher matcher = pattern.matcher(phoneNumber); // Check if pattern matches
   
        if(matcher.matches()) {
        	this.phoneNumber = phoneNumber;
        	return true;
        }
        else
        {
        	return false;
        }
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
}