package com.masaaroman.eessmobile.model;

import com.google.gson.annotations.SerializedName;

public class Department {

	@SerializedName("department_id")
	private int departmentId;
	
	@SerializedName("name")
	private String name;
	
	public Department(int departmentId, String name) {
		this.departmentId = departmentId;
		this.name = name;
	}
	
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	
	public int getDepartmentId() {
		return this.departmentId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}