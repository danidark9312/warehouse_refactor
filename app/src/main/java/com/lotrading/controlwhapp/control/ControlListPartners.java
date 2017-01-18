package com.lotrading.controlwhapp.control;

import java.util.ArrayList;

import com.lotrading.controlwhapp.model.ModelClient;
import com.lotrading.controlwhapp.model.ModelSupplier;


public class ControlListPartners {
	
	private ArrayList<ModelClient> listClients;
	private ArrayList<ModelSupplier> listSuppliers;
	
	public ControlListPartners() {
		listClients = new ArrayList<ModelClient>();
		listSuppliers = new ArrayList<ModelSupplier>();
	}
	
	public void addClient(ModelClient client){
		listClients.add(client);
	}
	
	public void addSupplier(ModelSupplier supplier){
		listSuppliers.add(supplier);
	}
	
	public void clearListClients(){
		listClients.clear();
	}
	
	public void clearListSuppliers(){
		listSuppliers.clear();
	}
	
	public ArrayList<ModelClient> getListClients() {
		return listClients;
	}
	
	public ArrayList<ModelSupplier> getListSuppliers() {
		return listSuppliers;
	}
	
	public ModelClient findClientIdByCode(String code){
		for (int i = 0; i < listClients.size(); i++) {
			ModelClient client = listClients.get(i);
			if(client.getCode().equals(code)){
				return client;
			}
		}
		return null;
	}
	
	public ModelSupplier findSupplierIdByName(String name){
		for (int i = 0; i < listSuppliers.size(); i++) {
			ModelSupplier supplier = listSuppliers.get(i);
			if(supplier.getName().equals(name)){
				return supplier;
			}
		}
		return null;
	}
	
}
