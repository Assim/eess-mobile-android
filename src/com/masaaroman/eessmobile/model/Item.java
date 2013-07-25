package com.masaaroman.eessmobile.model;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

public class Item {
	
	@SerializedName("item_id")
	private int itemId;
	
	@SerializedName("department_id")
	private int departmentId;
	
	@SerializedName("barcode")
	private int barcode;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("price")
	private BigDecimal price;
	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public int getItemId() {
		return this.itemId;
	}
	
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	
	public int getDepartmentId() {
		return this.departmentId;
	}
	
	public void setBarcode(int barcode) {
		this.barcode = barcode;
	}
	
	public int getBarcode() {
		return this.barcode;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getPrice() {
		return this.price;
	}
}