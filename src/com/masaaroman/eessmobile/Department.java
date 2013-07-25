package com.masaaroman.eessmobile;

import com.google.gson.annotations.SerializedName;

public class Department {

	@SerializedName("department_id")
	private int departmentId;
	
	@SerializedName("name")
	private String name;
	
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