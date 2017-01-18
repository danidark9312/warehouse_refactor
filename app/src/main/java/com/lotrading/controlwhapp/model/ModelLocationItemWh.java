package com.lotrading.controlwhapp.model;

import android.util.Log;

public class ModelLocationItemWh {
	
	private int nPieces;
	private String locationPlace;
	private int id;
	private String label;
	/**
	 * @param nPieces
	 * @param locationPlace
	 */
	public ModelLocationItemWh(int nPieces, String locationPlace,int id,String label) {
		Log.e("Este es la locacion", "Locacion: "+locationPlace);
		this.nPieces = nPieces;
		this.locationPlace = locationPlace;
		this.id = id;
		this.label=label;
	}
	/**
	 * @return the nPieces
	 */
	public int getnPieces() {
		return nPieces;
	}
	/**
	 * @param nPieces the nPieces to set
	 */
	public void setnPieces(int nPieces) {
		this.nPieces = nPieces;
	}

	public String getLocationPlace() {
		return locationPlace;
	}

	public void setLocationPlace(String locationPlace) {
		this.locationPlace = locationPlace;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the locationPlace
	 */


	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
}
