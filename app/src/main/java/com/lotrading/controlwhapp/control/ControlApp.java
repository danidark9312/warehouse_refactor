package com.lotrading.controlwhapp.control;

public class ControlApp {
	
	/*
	 * control logic application 
	 * instance all controllers
	 * use singleton pattern
	 */
	private static ControlApp instance = null;
	private ControlWhReceipt controlWhReceipt;
	private ControlListPartners controlListPartners;
	private ControlListCarrier controlListCarrier;
	private ControlListLocations controlListLocations;
	private ControlListMasterValues controlListMasterValues;
	private ControlSession controlSession;
	
	public static ControlApp getInstance(){
		if(instance == null){
			instance = new ControlApp();
		}
		return instance;
	}
	
	public ControlApp() {
		setControlSession(new ControlSession());
		controlWhReceipt = new ControlWhReceipt();
		controlListPartners = new ControlListPartners();
		controlListCarrier = new ControlListCarrier();
		controlListLocations = new ControlListLocations();
		controlListMasterValues = new ControlListMasterValues();
	}

	/**
	 * @return the controlWhReceipt
	 */
	public ControlWhReceipt getControlWhReceipt() {
		return controlWhReceipt;
	}

	/**
	 * @param controlWhReceipt the controlWhReceipt to set
	 */
	public void setControlWhReceipt(ControlWhReceipt controlWhReceipt) {
		this.controlWhReceipt = controlWhReceipt;
	}
	/**
	 * @return the controlListPartners
	 */
	public ControlListPartners getControlListPartners() {
		return controlListPartners;
	}
	/**
	 * @param controlListPartners the controlListPartners to set
	 */
	public void setControlListPartners(ControlListPartners controlListPartners) {
		this.controlListPartners = controlListPartners;
	}
	/**
	 * @return the controlListCarrier
	 */
	public ControlListCarrier getControlListCarrier() {
		return controlListCarrier;
	}
	/**
	 * @param controlListCarrier the controlListCarrier to set
	 */
	public void setControlListCarrier(ControlListCarrier controlListCarrier) {
		this.controlListCarrier = controlListCarrier;
	}
	/**
	 * @return the controlListLocations
	 */
	public ControlListLocations getControlListLocations() {
		return controlListLocations;
	}
	/**
	 * @param controlListLocations the controlListLocations to set
	 */
	public void setControlListLocations(ControlListLocations controlListLocations) {
		this.controlListLocations = controlListLocations;
	}
	/**
	 * @return the controlListMasterValues
	 */
	public ControlListMasterValues getControlListMasterValues() {
		return controlListMasterValues;
	}
	/**
	 * @param controlListMasterValues the controlListMasterValues to set
	 */
	public void setControlListMasterValues(ControlListMasterValues controlListMasterValues) {
		this.controlListMasterValues = controlListMasterValues;
	}
	/**
	 * @return the controlSession
	 */
	public ControlSession getControlSession() {
		return controlSession;
	}
	/**
	 * @param controlSession the controlSession to set
	 */
	public void setControlSession(ControlSession controlSession) {
		this.controlSession = controlSession;
	}
	
	
}
