package com.lotrading.controlwhapp.model;

public class ModelMasterValue {
	
	private int masterId;
	private int valueId;
	private String value;
	private String value2;
	private String value3;
	
	public ModelMasterValue(int masterId, int valueId, String value) {
		this.masterId = masterId;
		this.valueId = valueId;
		this.value = value;
		this.value2 = "";
		this.value3 = "";
	}
	
	
	/**
	 * @param masterId
	 * @param valueId
	 * @param value
	 * @param value2
	 * @param value3
	 */
	public ModelMasterValue(int masterId, int valueId, String value, String value2, String value3) {
		super();
		this.masterId = masterId;
		this.valueId = valueId;
		this.value = value;
		this.value2 = value2;
		this.value3 = value3;
	}


	/**
	 * @return the masterId
	 */
	public int getMasterId() {
		return masterId;
	}
	/**
	 * @param masterId the masterId to set
	 */
	public void setMasterId(int masterId) {
		this.masterId = masterId;
	}
	/**
	 * @return the valueId
	 */
	public int getValueId() {
		return valueId;
	}
	/**
	 * @param valueId the valueId to set
	 */
	public void setValueId(int valueId) {
		this.valueId = valueId;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @return the value2
	 */
	public String getValue2() {
		return value2;
	}


	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(String value2) {
		this.value2 = value2;
	}


	/**
	 * @return the value3
	 */
	public String getValue3() {
		return value3;
	}


	/**
	 * @param value3 the value3 to set
	 */
	public void setValue3(String value3) {
		this.value3 = value3;
	}
	
}
