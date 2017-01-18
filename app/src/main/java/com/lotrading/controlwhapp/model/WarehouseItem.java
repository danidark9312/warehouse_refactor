package com.lotrading.controlwhapp.model;



import java.util.List;

/**
 * Created by daniel on 08/01/2017.
 */

public class WarehouseItem{

    private String idItem;
    private String itemNumber;
    private String clientDescription;
    private String clientRef;
    private String supplierDescription;
    private String supplierRef;
    private String idUnitType;
    private String unitType;
    private String idProduct;
    private String productName;
    private String manufacturerRef;
    private String orderToPlaceItemId;
    private String statusItem;
    private String hazardaous;
    private String urgent;
    private String idDepartment;

    private int quantity;
    private int quantityArrived;
    private int quantityEntered;
    private int quantityArrivedKG;

    private Double weigth;
    private Double weigthKg;
    private Double volumeInches;
    private Integer pieces;
    private String nameType;
    private String idType;
    private String length;
    private String width;
    private String height;
    private String remarks;
    private String poItemId;
    private String worksheetId;
    private List<Location> locations;
    private List<Tracking> trackings;
    private int isCompleted;

    public String getIdItem() {
        return idItem;
    }
    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }
    public String getItemNumber() {
        return itemNumber;
    }
    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
    public String getIdUnitType() {
        return idUnitType;
    }
    public void setIdUnitType(String idUnitType) {
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
    public String getUrgent() {
        return urgent;
    }
    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }
    public String getIdDepartment() {
        return idDepartment;
    }
    public void setIdDepartment(String idDepartment) {
        this.idDepartment = idDepartment;
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
    public String getLength() {
        return length;
    }
    public void setLength(String length) {
        this.length = length;
    }
    public String getWidth() {
        return width;
    }
    public void setWidth(String width) {
        this.width = width;
    }
    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
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
    public List<Location> getLocations() {
        return locations;
    }
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
    public List<Tracking> getTrackings() {
        return trackings;
    }
    public void setTrackings(List<Tracking> trackings) {
        this.trackings = trackings;
    }
    public Double getVolumeInches() {
        return volumeInches;
    }
    public void setVolumeInches(Double volumeInches) {
        this.volumeInches = volumeInches;
    }
    public int getQuantityArrived() {
        return quantityArrived;
    }
    public void setQuantityArrived(int quantityArrived) {
        this.quantityArrived = quantityArrived;
    }
    public int getQuantityEntered() {
        return quantityEntered;
    }
    public void setQuantityEntered(int quantityEntered) {
        this.quantityEntered = quantityEntered;
    }
    public String getWorksheetId() {
        return worksheetId;
    }
    public void setWorksheetId(String worksheetId) {
        this.worksheetId = worksheetId;
    }
    public int getIsCompleted() {
        return isCompleted;
    }
    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }
    public int getQuantityArrivedKG() {
        return quantityArrivedKG;
    }
    public void setQuantityArrivedKG(int quantityArrivedKG) {
        this.quantityArrivedKG = quantityArrivedKG;
    }
}