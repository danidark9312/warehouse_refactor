package com.lotrading.controlwhapp.model;

import java.util.List;



public class WarehouseItem{

	private Integer idItem;
	private String itemNumber;
	private String clientDescription;
	private String clientRef;
	private String supplierDescription;
	private String supplierRef;
	private Integer idUnitType;
	private String unitType;
	private String idProduct;
	private String productName;
	private String manufacturerRef;
	private String orderToPlaceItemId;
	private String statusItem;
	private String hazardaous;
	private String hazString;
	private String urgent;
	private Integer idDepartment;
	
	private double quantity;
	private double quantityArrived;
	private double quantityEntered;
	private double quantityArrivedKG;
	
	private Double weigth;
	private Double weigthKg;
	private Double volumeInches;
	private Integer pieces;
	private String nameType;
	private String idType;
	private Double length;
	private Double width;
	private Double height;
	private String remarks;
	private String poItemId;
	private String worksheetId;
	private List<Location> locations;
	private String locationsString;
	private List<Tracking> trackings;
	private String trackingsString;
	private Integer isCompleted;
	public Integer getIdItem() {
		return idItem;
	}
	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getClientDescription() {
		return clientDescription;
	}
	public void setClientDescription(String clientDescription) {
		this.clientDescription = clientDescription;
	}
	public String getClientRef() {
		return clientRef;
	}
	public void setClientRef(String clientRef) {
		this.clientRef = clientRef;
	}
	public String getSupplierDescription() {
		return supplierDescription;
	}
	public void setSupplierDescription(String supplierDescription) {
		this.supplierDescription = supplierDescription;
	}
	public String getSupplierRef() {
		return supplierRef;
	}
	public void setSupplierRef(String supplierRef) {
		this.supplierRef = supplierRef;
	}
	public Integer getIdUnitType() {
		return idUnitType;
	}
	public void setIdUnitType(Integer idUnitType) {
		this.idUnitType = idUnitType;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getManufacturerRef() {
		return manufacturerRef;
	}
	public void setManufacturerRef(String manufacturerRef) {
		this.manufacturerRef = manufacturerRef;
	}
	public String getOrderToPlaceItemId() {
		return orderToPlaceItemId;
	}
	public void setOrderToPlaceItemId(String orderToPlaceItemId) {
		this.orderToPlaceItemId = orderToPlaceItemId;
	}
	public String getStatusItem() {
		return statusItem;
	}
	public void setStatusItem(String statusItem) {
		this.statusItem = statusItem;
	}
	public String getHazardaous() {
		return hazardaous;
	}
	public void setHazardaous(String hazardaous) {
		this.hazardaous = hazardaous;
	}
	public String getHazString() {
		return hazString;
	}
	public void setHazString(String hazString) {
		this.hazString = hazString;
	}
	public String getUrgent() {
		return urgent;
	}
	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}
	public Integer getIdDepartment() {
		return idDepartment;
	}
	public void setIdDepartment(Integer idDepartment) {
		this.idDepartment = idDepartment;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getQuantityArrived() {
		return quantityArrived;
	}

	public void setQuantityArrived(double quantityArrived) {
		this.quantityArrived = quantityArrived;
	}

	public double getQuantityEntered() {
		return quantityEntered;
	}

	public void setQuantityEntered(double quantityEntered) {
		this.quantityEntered = quantityEntered;
	}

	public double getQuantityArrivedKG() {
		return quantityArrivedKG;
	}

	public void setQuantityArrivedKG(double quantityArrivedKG) {
		this.quantityArrivedKG = quantityArrivedKG;
	}

	public void setQuantityArrivedKG(Integer quantityArrivedKG) {
		this.quantityArrivedKG = quantityArrivedKG;
	}
	public Double getWeigth() {
		return weigth;
	}
	public void setWeigth(Double weigth) {
		this.weigth = weigth;
	}
	public Double getWeigthKg() {
		return weigthKg;
	}
	public void setWeigthKg(Double weigthKg) {
		this.weigthKg = weigthKg;
	}
	public Double getVolumeInches() {
		return volumeInches;
	}
	public void setVolumeInches(Double volumeInches) {
		this.volumeInches = volumeInches;
	}
	public Integer getPieces() {
		return pieces;
	}
	public void setPieces(Integer pieces) {
		this.pieces = pieces;
	}
	public String getNameType() {
		return nameType;
	}
	public void setNameType(String nameType) {
		this.nameType = nameType;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPoItemId() {
		return poItemId;
	}
	public void setPoItemId(String poItemId) {
		this.poItemId = poItemId;
	}
	public String getWorksheetId() {
		return worksheetId;
	}
	public void setWorksheetId(String worksheetId) {
		this.worksheetId = worksheetId;
	}
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	public String getLocationsString() {
		return locationsString;
	}
	public void setLocationsString(String locationsString) {
		this.locationsString = locationsString;
	}
	public List<Tracking> getTrackings() {
		return trackings;
	}
	public void setTrackings(List<Tracking> trackings) {
		this.trackings = trackings;
	}
	public String getTrackingsString() {
		return trackingsString;
	}
	public void setTrackingsString(String trackingsString) {
		this.trackingsString = trackingsString;
	}
	public Integer getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(Integer isCompleted) {
		this.isCompleted = isCompleted;
	}
}
