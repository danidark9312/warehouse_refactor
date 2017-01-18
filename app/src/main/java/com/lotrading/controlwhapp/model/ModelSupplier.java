package com.lotrading.controlwhapp.model;

public class ModelSupplier {
	
	private int idSupplier;
	private String name;
	
	public ModelSupplier(int idSupplier, String name) {
		this.idSupplier = idSupplier;
		this.name = name;
	}

	/**
	 * @return the idSupplier
	 */
	public int getIdSupplier() {
		return idSupplier;
	}

	/**
	 * @param idSupplier the idSupplier to set
	 */
	public void setIdSupplier(int idSupplier) {
		this.idSupplier = idSupplier;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
