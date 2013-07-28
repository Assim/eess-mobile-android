package com.masaaroman.eessmobile.model;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

public class CartItem {
	
	@SerializedName("id")
	private int id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("unit_price")
	private BigDecimal unitPrice;
	
	@SerializedName("qty")
	private int qty;
	
	public CartItem(int id, String name, BigDecimal unitPrice, int qty) {
		this.id = id;
		this.name = name;
		this.unitPrice = unitPrice;
		this.qty = qty;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}
	
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public int getQty() {
		return this.qty;
	}
}