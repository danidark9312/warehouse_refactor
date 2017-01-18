package com.lotrading.controlwhapp.control;

import com.lotrading.controlwhapp.model.ModelWhReceipt;

public class ControlWhReceipt {
	
	private ModelWhReceipt modelWhReceipt;
	
	public ControlWhReceipt() {
		this.modelWhReceipt = null;
	}
	
	public void createNewWhReceipt(String id, String idno, String po, String rep, int idClient, String nameClient, int idSupplier, String nameSupplier,
			int idDep, String dep){
		
		String idEmployeeEntered = ControlApp.getInstance().getControlSession().getEmployeeSession().getId();
		String nameEmployeeEntered = ControlApp.getInstance().getControlSession().getEmployeeSession().getCompleteName();
		
		this.modelWhReceipt = new ModelWhReceipt(id, idno, po, rep, idClient, nameClient, idSupplier, nameSupplier, idDep, dep, idEmployeeEntered, nameEmployeeEntered);
	}
	
	public ModelWhReceipt getModelWhReceipt() {
		return modelWhReceipt;
	}
	
}
