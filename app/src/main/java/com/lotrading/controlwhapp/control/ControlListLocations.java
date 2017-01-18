package com.lotrading.controlwhapp.control;

import java.util.ArrayList;

import com.lotrading.controlwhapp.model.ModelLocation;

public class ControlListLocations {
	
	private ArrayList<ModelLocation> listLocations; //list for unit type
	
	public ControlListLocations() {
		listLocations = new ArrayList<ModelLocation>();
	}
	
	public void addLocation(ModelLocation location){
		listLocations.add(location);
	}
	
	public ArrayList<ModelLocation> getlistLocations() {
		return listLocations;
	}
	
}
