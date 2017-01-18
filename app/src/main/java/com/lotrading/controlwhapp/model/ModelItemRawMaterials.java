package com.lotrading.controlwhapp.model;

import java.util.ArrayList;

public class ModelItemRawMaterials {
	
	private int idItem;
	private int itemNumber;
	private String productName;
	private String productRef;
	private double quantity;
	private String unit; //lb, kg
	private double qtyArrivedLB;
	private double qtyArrivedKG;
	private int nPieces;
	private int orderToPlace;
	private ModelMasterValue unitType; //box, pallet etc
	private int positionUnitType;
	private boolean isUrgent;

	private boolean hazmat;
	private double length;
	private double width;
	private double height;
	private String remarks;

	private double weigthLB;
	private double weigthKG;

	private ArrayList<ModelLocationItemWh> listLocations;
	private ArrayList<String> listTrackings;



	private int iscompleted;

	public ArrayList<ModelLocationItemWh> getListLocations() {
		return listLocations;
	}

	public void setListLocations(ArrayList<ModelLocationItemWh> listLocations) {
		this.listLocations = listLocations;
	}

	public ArrayList<String> getListTrackings() {
		return listTrackings;
	}

	public void setListTrackings(ArrayList<String> listTrackings) {
		this.listTrackings = listTrackings;
	}

	/**
	 * @param idItem
	 * @param itemNumber
	 * @param productName
	 * @param productRef
	 * @param quantity
	 * @param unit
	 */



	public ModelItemRawMaterials(int idItem, int itemNumber, String productName, String productRef, double quantity, int orderToPlace, String unit) {
		this.idItem = idItem;
		this.itemNumber = itemNumber;
		this.productName = productName;
		this.productRef = productRef;
		this.quantity = quantity;
		this.unit = unit;
		this.qtyArrivedLB = 0L;
		this.qtyArrivedKG = 0L;
		this.nPieces = 0;
		this.orderToPlace
				= orderToPlace;
		this.unitType = new ModelMasterValue(0, 0, "");
		this.positionUnitType = 0;
		this.setIscompleted(0);
	}
	public ModelItemRawMaterials(int idItem, int itemNumber, String productName, String productRef, double quantity, int orderToPlace, String unit,boolean haz) {
		this.idItem = idItem;
		this.itemNumber = itemNumber;
		this.productName = productName;
		this.productRef = productRef;
		this.quantity = quantity;
		this.unit = unit;
		this.qtyArrivedLB = 0L;
		this.qtyArrivedKG = 0L;
		this.nPieces = 0;
		this.orderToPlace
				= orderToPlace;
		this.unitType = new ModelMasterValue(0, 0, "");
		this.positionUnitType = 0;
		this.setIscompleted(0);
		this.hazmat = haz;

	}

	public boolean isUrgent() {
		return isUrgent;
	}

	public void setUrgent(boolean urgent) {
		isUrgent = urgent;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isHazmat() {
		return hazmat;
	}

	public void setHazmat(boolean hazmat) {
		this.hazmat = hazmat;
	}


	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeigthLB() {
		return weigthLB;
	}

	public void setWeigthLB(double weigthLB) {
		this.weigthLB = weigthLB;
	}

	public double getWeigthKG() {
		return weigthKG;
	}

	public void setWeigthKG(double weigthKG) {
		this.weigthKG = weigthKG;
	}

	/**
	 * @return the idItem
	 */
	public int getIdItem() {
		return idItem;
	}
	/**
	 * @param idItem the idItem to set
	 */
	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}
	/**
	 * @return the itemNumber
	 */
	public int getItemNumber() {
		return itemNumber;
	}
	/**
	 * @param itemNumber the itemNumber to set
	 */
	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the productRef
	 */
	public String getProductRef() {
		return productRef;
	}
	/**
	 * @param productRef the productRef to set
	 */
	public void setProductRef(String productRef) {
		this.productRef = productRef;
	}
	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the qtyArrivedLB
	 */
	public double getQtyArrivedLB() {
		return qtyArrivedLB;
	}
	/**
	 * @param qtyArrivedLB the qtyArrivedLB to set
	 */
	public void setQtyArrivedLB(double qtyArrivedLB) {
		this.qtyArrivedLB = qtyArrivedLB;
	}
	/**
	 * @return the qtyArrivedKG
	 */
	public double getQtyArrivedKG() {
		return qtyArrivedKG;
	}
	/**
	 * @param qtyArrivedKG the qtyArrivedKG to set
	 */
	public void setQtyArrivedKG(double qtyArrivedKG) {
		this.qtyArrivedKG = qtyArrivedKG;
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
	/**
	 * @return the unitType
	 */
	public ModelMasterValue getUnitType() {
		return unitType;
	}
	/**
	 * @param unitType the unitType to set
	 */
	public void setUnitType(ModelMasterValue unitType) {
		this.unitType = unitType;
	}


	/**
	 * @return the orderToPlace
	 */
	public int getOrderToPlace() {
		return orderToPlace;
	}


	/**
	 * @param orderToPlace the orderToPlace to set
	 */
	public void setOrderToPlace(int orderToPlace) {
		this.orderToPlace = orderToPlace;
	}


	/**
	 * @return the positionUnitType
	 */
	public int getPositionUnitType() {
		return positionUnitType;
	}


	/**
	 * @param positionUnitType the positionUnitType to set
	 */
	public void setPositionUnitType(int positionUnitType) {
		this.positionUnitType = positionUnitType;
	}


	/**
	 * @return the iscompleted
	 */
	public int getIscompleted() {
		return iscompleted;
	}


	/**
	 * @param iscompleted the iscompleted to set
	 */
	public void setIscompleted(int iscompleted) {
		this.iscompleted = iscompleted;
	}


	public void setDimensions(int length,int width,int height){
		this.length = length;
		this.width = width;
		this.height = height;
	}
	
	
}
