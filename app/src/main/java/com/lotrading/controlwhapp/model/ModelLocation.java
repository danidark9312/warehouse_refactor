package com.lotrading.controlwhapp.model;

public class ModelLocation {
	
	private String location;
	private int id;
	/**
	 * @param location
	 * @param rack
	 * @param floor
	 */
	public ModelLocation(String location, int id) {
		this.setLocation(location);
		this.setId(id);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	 

}
