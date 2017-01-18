package com.lotrading.controlwhapp.model;

public class ModelCarrier {
	
	private int idCarrier;
	private String carrier;

	/**
	 * @param idCarrier
	 * @param carrier
	 */
	public ModelCarrier(int idCarrier, String carrier) {
		this.idCarrier = idCarrier;
		this.carrier = carrier;
	}
	/**
	 * @return the idCarrier
	 */
	public int getIdCarrier() {
		return idCarrier;
	}
	/**
	 * @param idCarrier the idCarrier to set
	 */
	public void setIdCarrier(int idCarrier) {
		this.idCarrier = idCarrier;
	}
	/**
	 * @return the carrier
	 */
	public String getCarrier() {
		return carrier;
	}
	/**
	 * @param carrier the carrier to set
	 */
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	
}
