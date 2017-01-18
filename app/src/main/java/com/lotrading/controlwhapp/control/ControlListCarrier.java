package com.lotrading.controlwhapp.control;

import java.util.ArrayList;

import com.lotrading.controlwhapp.model.ModelCarrier;

public class ControlListCarrier {
	
	private ArrayList<ModelCarrier> listTruckCompany; //list for unit type
	
	public ControlListCarrier() {
		listTruckCompany = new ArrayList<ModelCarrier>();
	}
	
	public void addTruckCompany(ModelCarrier truckCompany){
		listTruckCompany.add(truckCompany);
	}
	
	public ArrayList<ModelCarrier> getListTruckCompany() {
		return listTruckCompany;
	}
	
	public ModelCarrier findTruckCompanyByName(String name){
		for (int i = 0; i < listTruckCompany.size(); i++) {
			ModelCarrier truck = listTruckCompany.get(i);
			if(truck.getCarrier().equals(name)){
				return truck;
			}
		}
		return null;
	}
	
}
