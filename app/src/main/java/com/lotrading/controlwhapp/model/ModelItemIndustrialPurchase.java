package com.lotrading.controlwhapp.model;

public class ModelItemIndustrialPurchase {
	
	private int idItem;
	private int itemNumber;
	private double quantity;
	private String productClientDesciption;
	private String productClientRef;
	private String productSupplierDescription;
	private String productSupplierRef;
	private int idUnitType;
	private String unitType;
	private String idProduct;
	private String productName;
	private String manufacturerRef;
	private double qtyArrived;
	private double qtyEntered;
	private String remarks;
	
	
	/**
	 * @param idItem
	 * @param itemNumber
	 * @param quantity
	 * @param productClientDesciption
	 * @param productClientRef
	 * @param productSupplierDescription
	 * @param productSupplierRef
	 * @param idUnitType
	 * @param unitType
	 * @param idProduct
	 * @param productName
	 * @param manufacturerRef
	 * @param qtyArrived
	 */
	public ModelItemIndustrialPurchase(int idItem, int itemNumber, int quantity,
			String productClientDesciption, String productClientRef,
			String productSupplierDescription, String productSupplierRef,
			int idUnitType, String unitType, String idProduct, String productName,
			String manufacturerRef, double qtyArrived) {
		this.idItem = idItem;
		this.itemNumber = itemNumber;
		this.quantity = quantity;
		this.productClientDesciption = productClientDesciption;
		this.productClientRef = productClientRef;
		this.productSupplierDescription = productSupplierDescription;
		this.productSupplierRef = productSupplierRef;
		this.idUnitType = idUnitType;
		this.unitType = unitType;
		this.idProduct = idProduct;
		this.productName = productName;
		this.manufacturerRef = manufacturerRef;
		this.qtyArrived = qtyArrived;
		this.qtyEntered = 0;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the productClientDesciption
	 */
	public String getProductClientDesciption() {
		return productClientDesciption;
	}

	/**
	 * @param productClientDesciption the productClientDesciption to set
	 */
	public void setProductClientDesciption(String productClientDesciption) {
		this.productClientDesciption = productClientDesciption;
	}

	/**
	 * @return the productClientRef
	 */
	public String getProductClientRef() {
		return productClientRef;
	}

	/**
	 * @param productClientRef the productClientRef to set
	 */
	public void setProductClientRef(String productClientRef) {
		this.productClientRef = productClientRef;
	}

	/**
	 * @return the productSupplierDescription
	 */
	public String getProductSupplierDescription() {
		return productSupplierDescription;
	}

	/**
	 * @param productSupplierDescription the productSupplierDescription to set
	 */
	public void setProductSupplierDescription(String productSupplierDescription) {
		this.productSupplierDescription = productSupplierDescription;
	}

	/**
	 * @return the productSupplierRef
	 */
	public String getProductSupplierRef() {
		return productSupplierRef;
	}

	/**
	 * @param productSupplierRef the productSupplierRef to set
	 */
	public void setProductSupplierRef(String productSupplierRef) {
		this.productSupplierRef = productSupplierRef;
	}

	/**
	 * @return the idUnitType
	 */
	public int getIdUnitType() {
		return idUnitType;
	}

	/**
	 * @param idUnitType the idUnitType to set
	 */
	public void setIdUnitType(int idUnitType) {
		this.idUnitType = idUnitType;
	}

	/**
	 * @return the unitType
	 */
	public String getUnitType() {
		return unitType;
	}

	/**
	 * @param unitType the unitType to set
	 */
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	/**
	 * @return the idProduct
	 */
	public String getIdProduct() {
		return idProduct;
	}

	/**
	 * @param idProduct the idProduct to set
	 */
	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
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
	 * @return the manufacturerRef
	 */
	public String getManufacturerRef() {
		return manufacturerRef;
	}

	/**
	 * @param manufacturerRef the manufacturerRef to set
	 */
	public void setManufacturerRef(String manufacturerRef) {
		this.manufacturerRef = manufacturerRef;
	}

	/**
	 * @param qtyArrived the qtyArrived to set
	 */
	public void setQtyArrived(int qtyArrived) {
		this.qtyArrived = qtyArrived;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getQtyArrived() {
		return qtyArrived;
	}

	public void setQtyArrived(double qtyArrived) {
		this.qtyArrived = qtyArrived;
	}

	public double getQtyEntered() {
		return qtyEntered;
	}

	public void setQtyEntered(double qtyEntered) {
		this.qtyEntered = qtyEntered;
	}

	/**
	 * @param qtyEntered the qtyEntered to set
	 */
	public void setQtyEntered(int qtyEntered) {
		this.qtyEntered = qtyEntered;
	}

	
}
