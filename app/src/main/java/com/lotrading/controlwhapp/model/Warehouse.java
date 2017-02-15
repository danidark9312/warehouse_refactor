package com.lotrading.controlwhapp.model;

import java.util.List;



public class Warehouse  {

	private String whReceiptId;
	private String whReceiptNumber;
	
	
	
	private String poNumber;
	private Integer idPo;
	private Integer idDepartment;
	private Integer idSupplier;
	private Integer idClient;
	private Integer idEmployee;
	private String dateReceived;
	private Integer idGroup;
	private Integer idStatus;
	private Integer statusChanged;
	private List<WarehouseItem> wareHouseItems;
	private List<WarehouseItem> departmentItems;
	private Integer numeroLabels;
	
	
	private String idWhNumber;
	private String whNumber;
	private String clientName;
	private String supplierName;
	private String truckId;
	private String truckName;
	private String htPallets;
	private String remarks;
	private String dateReceipt;
	private String employeeEntered; 
	
	
	private Double sumPcWeightLb;
	private Double sumPcWeightKg;
	private Double sumPcVolumeIn;
	private Integer pieces;
	private Double weightVolume;
	private Double volumeCubeMeter;
	
	
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	
	public String getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}
	public String getWhReceiptId() {
		return whReceiptId;
	}
	public void setWhReceiptId(String whReceiptId) {
		this.whReceiptId = whReceiptId;
	}
	public String getWhReceiptNumber() {
		return whReceiptNumber;
	}
	public void setWhReceiptNumber(String whReceiptNumber) {
		this.whReceiptNumber = whReceiptNumber;
	}
	public List<WarehouseItem> getWareHouseItems() {
		return wareHouseItems;
	}
	public void setWareHouseItems(List<WarehouseItem> wareHouseItems) {
		this.wareHouseItems = wareHouseItems;
	}
	public String getIdWhNumber() {
		return idWhNumber;
	}
	public void setIdWhNumber(String idWhNumber) {
		this.idWhNumber = idWhNumber;
	}
	public String getWhNumber() {
		return whNumber;
	}
	public void setWhNumber(String whNumber) {
		this.whNumber = whNumber;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}
	public String getTruckName() {
		return truckName;
	}
	public void setTruckName(String truckName) {
		this.truckName = truckName;
	}
	public String getHtPallets() {
		return htPallets;
	}
	public void setHtPallets(String htPallets) {
		this.htPallets = htPallets;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDateReceipt() {
		return dateReceipt;
	}
	public void setDateReceipt(String dateReceipt) {
		this.dateReceipt = dateReceipt;
	}
	public String getEmployeeEntered() {
		return employeeEntered;
	}
	public void setEmployeeEntered(String employeeEntered) {
		this.employeeEntered = employeeEntered;
	}
	public Double getSumPcWeightLb() {
		return sumPcWeightLb;
	}
	public void setSumPcWeightLb(Double sumPcWeightLb) {
		this.sumPcWeightLb = sumPcWeightLb;
	}
	public Double getSumPcWeightKg() {
		return sumPcWeightKg;
	}
	public void setSumPcWeightKg(Double sumPcWeightKg) {
		this.sumPcWeightKg = sumPcWeightKg;
	}
	public Double getSumPcVolumeIn() {
		return sumPcVolumeIn;
	}
	public void setSumPcVolumeIn(Double sumPcVolumeIn) {
		this.sumPcVolumeIn = sumPcVolumeIn;
	}
	public Integer getPieces() {
		return pieces;
	}
	public void setPieces(Integer pieces) {
		this.pieces = pieces;
	}
	public Double getWeightVolume() {
		return weightVolume;
	}
	public void setWeightVolume(Double weightVolume) {
		this.weightVolume = weightVolume;
	}
	public Double getVolumeCubeMeter() {
		return volumeCubeMeter;
	}
	public void setVolumeCubeMeter(Double volumeCubeMeter) {
		this.volumeCubeMeter = volumeCubeMeter;
	}
	public Integer getIdPo() {
		return idPo;
	}
	public void setIdPo(Integer idPo) {
		this.idPo = idPo;
	}
	public Integer getIdDepartment() {
		return idDepartment;
	}
	public void setIdDepartment(Integer idDepartment) {
		this.idDepartment = idDepartment;
	}
	public Integer getIdSupplier() {
		return idSupplier;
	}
	public void setIdSupplier(Integer idSupplier) {
		this.idSupplier = idSupplier;
	}
	public Integer getIdClient() {
		return idClient;
	}
	public void setIdClient(Integer idClient) {
		this.idClient = idClient;
	}
	public Integer getIdEmployee() {
		return idEmployee;
	}
	public void setIdEmployee(Integer idEmployee) {
		this.idEmployee = idEmployee;
	}
	public Integer getIdGroup() {
		return idGroup;
	}
	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public Integer getStatusChanged() {
		return statusChanged;
	}
	public void setStatusChanged(Integer statusChanged) {
		this.statusChanged = statusChanged;
	}
	public Integer getNumeroLabels() {
		return numeroLabels;
	}
	public void setNumeroLabels(Integer numeroLabels) {
		this.numeroLabels = numeroLabels;
	}
	public List<WarehouseItem> getDepartmentItems() {
		return departmentItems;
	}
	public void setDepartmentItems(List<WarehouseItem> departmentItems) {
		this.departmentItems = departmentItems;
	}
}
