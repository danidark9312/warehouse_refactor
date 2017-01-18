package com.lotrading.controlwhapp.control;

import java.util.ArrayList;

import com.lotrading.controlwhapp.model.ModelMasterValue;

public class ControlListMasterValues {
	
	private ArrayList<ModelMasterValue> listUnitType; //list for unit type
	private ArrayList<ModelMasterValue> listPrinters; //list for unit type
	
	public ControlListMasterValues() {
		listUnitType = new ArrayList<ModelMasterValue>();
		listPrinters = new ArrayList<ModelMasterValue>();
	}
	
	public void addUnitType(ModelMasterValue unitType){
		listUnitType.add(unitType);
	}
	
	public void addPrinter(ModelMasterValue printer){
		listPrinters.add(printer);
	}
	
	public ArrayList<ModelMasterValue> getListUnitType() {
		return listUnitType;
	}
	
	public ArrayList<ModelMasterValue> getListPrinters() {
		return listPrinters;
	}
	
	public ModelMasterValue obtainUnitTypeById(int valueId){
		for (int i = 0; i < listUnitType.size(); i++) {
			if(listUnitType.get(i).getValueId() == valueId){
				return listUnitType.get(i);
			}
		}
		return null;
	}
	
}
