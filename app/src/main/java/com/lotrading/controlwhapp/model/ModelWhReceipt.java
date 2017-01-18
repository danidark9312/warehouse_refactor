package com.lotrading.controlwhapp.model;

import java.util.ArrayList;

import com.lotrading.controlwhapp.config.Config;

public class ModelWhReceipt {
	
	//basic info constructor
	private String idPODepartment;
	private String idno;
	private String po;
	private String rep;
	private int idClient;
	private String nameClient;
	private int idSupplier;
	private String nameSupplier;
	private int idDep;
	private String dep;
	private String idEmployeeEntered;
	private String nameEmployeeEntered;

	
	//data wh receipt
	private int status;
	private String nameStatus;
	private ArrayList<ModelItemWhReceipt> listItemsWhLO;
	private String remarks;
	private ModelCarrier truckCompany;
	private int htPallets;
	
	//date after create in datatable
	private String idWhReceipt;
	private String WhReceiptNumber;
	private String dateTimeReceived;
	
	//data images and docs paths
	private ArrayList<ModelFileUpload> listPhotosWh;
	private ArrayList<ModelFileUpload> listDocsWh;
	
	//data labels
	private int numLabels; 
	private String urlLabelBase;
	
	//data items purchase order
	private ArrayList<ModelItemIndustrialPurchase> listItemsIndustrialPurchase;
	private boolean isSavedIP;// used for show or hide popup IP
	
	//data items raw materials
	private ArrayList<ModelItemRawMaterials> listItemsRawMaterials;
	private boolean isSavedRM;// used for show or hide popup IP
	

	public ModelWhReceipt(String idPODepartment, String idno, String po, String rep, int idClient, String nameClient, int idSupplier, String nameSupplier, int idDep, String dep, String idEmployeeEntered, String nameEmployeeEntered) {
		this.idPODepartment = idPODepartment;
		this.idno = idno;
		this.po = po;
		this.rep = rep;
		this.idClient = idClient;
		this.nameClient = nameClient;
		this.idSupplier = idSupplier;
		this.nameSupplier = nameSupplier;
		this.idDep = idDep;
		this.dep = dep;
		this.idEmployeeEntered = idEmployeeEntered;
		this.nameEmployeeEntered = nameEmployeeEntered;
		
		status = 1; //in warehouse
		nameStatus = "In";
		
		listItemsWhLO = new ArrayList<ModelItemWhReceipt>(); //init empty array items wh always
		listPhotosWh = new ArrayList<ModelFileUpload>();
		listDocsWh = new ArrayList<ModelFileUpload>();
		listItemsIndustrialPurchase = new ArrayList<ModelItemIndustrialPurchase>();
		setListItemsRawMaterials(new ArrayList<ModelItemRawMaterials>());
		
		truckCompany = new ModelCarrier(0, ""); //empty truck company
		remarks = "";
		htPallets = -1;
		urlLabelBase = Config.SERVER_IP + "/ControlWarehouse/file-system/WH-RECEIPTS/";

		this.isSavedIP = false;
		this.isSavedRM = false;
	}

	public String getWhReceiptNumber() {
		return WhReceiptNumber;
	}

	public void setWhReceiptNumber(String whReceiptNumber) {
		WhReceiptNumber = whReceiptNumber;
	}

	public void addItemToListItemsWhLO(ModelItemWhReceipt itemWh){
		listItemsWhLO.add(itemWh);
	}
	
	public void replaceItemToListWhLo(int pos, ModelItemWhReceipt itemWh){
		try{
			ModelItemWhReceipt oldItem = listItemsWhLO.get(pos);
			itemWh.setCloned(oldItem.isCloned());
			itemWh.setRelationIdRMItem(oldItem.getRelationIdRMItem());
			listItemsWhLO.set(pos, itemWh);
		}catch(Exception e){
			System.err.println("error editing item wh receipt");
		}
	}
	
	public void removeItemToListWhLo(int pos){
		try{
			listItemsWhLO.remove(pos);
		}catch(Exception e){
			System.err.println("error removing item wh receipt");
		}
	}
	
	public void addPhotoToList(ModelFileUpload file){
		try{
			listPhotosWh.add(file);
		}catch(Exception e){
			System.err.println("error add file addPhotoToList");
		}
	}
	
	public void addDocToList(ModelFileUpload file){
		try{
			listDocsWh.add(file);
		}catch(Exception e){
			System.err.println("error add file addDocToList");
		}
	}
	
	public void addItemIndustrialPurchase(ModelItemIndustrialPurchase item){
		try{
			listItemsIndustrialPurchase.add(item);
		}catch(Exception e){
			System.err.println("error add item industrial purchases");
		}
	}
	
	public void addItemRawMaterials(ModelItemRawMaterials item){
		try{
			listItemsRawMaterials.add(item);
		}catch(Exception e){
			System.err.println("error add item row materials");
		}
	}
	
	public int changeQuantityItemIP(int position, int qty){
		try{
			ModelItemIndustrialPurchase itemIP = listItemsIndustrialPurchase.get(position);
			itemIP.setQtyEntered(qty);
			if(itemIP.getQuantity() == (itemIP.getQtyArrived() + itemIP.getQtyEntered())){
				return 0;
			}else if(itemIP.getQuantity() > (itemIP.getQtyArrived() + itemIP.getQtyEntered()) ) {
				return 1;
			}
			return -1; //it cannot be inserted
		}catch(Exception e){
			System.err.println("error changing qty changeQuantityItemIP ");
			return -2; //error
		}
	}
	
	public void changeQuantityArrivedRM(int position, double qtyArrivedLB, double qtyArrivedKG) {
		try{
			ModelItemRawMaterials itemRM = listItemsRawMaterials.get(position);
			itemRM.setQtyArrivedLB(qtyArrivedLB);
			itemRM.setQtyArrivedKG(qtyArrivedKG);
		}catch(Exception e){
			System.err.println("error changing qty changeQuantityArrivedRM ");
		}
	}
	
	public void changeNPiecesArrivedRM(int position, int nPieces) {
		try{
			ModelItemRawMaterials itemRM = listItemsRawMaterials.get(position);
			itemRM.setnPieces(nPieces);
		}catch(Exception e){
			System.err.println("error changing qty changeNPiecesArrivedRM ");
		}
	}

	public void changeRemarksRM(int position, String remarks) {
		try{
			ModelItemRawMaterials itemRM = listItemsRawMaterials.get(position);
			itemRM.setRemarks(remarks);
		}catch(Exception e){
			System.err.println("error changing qty changeNPiecesArrivedRM ");
		}
	}

	//clone item
	public void cloneItemToListWhLo(int currentIdItem) {
		try{
			ModelItemWhReceipt itemCurrent = listItemsWhLO.get(currentIdItem);
			ModelItemWhReceipt itemCloned = (ModelItemWhReceipt) itemCurrent.clone();
			itemCloned.setCloned(true); //important!!!!!!!
			ArrayList<ModelLocationItemWh> cloneLocations = (ArrayList<ModelLocationItemWh>)itemCurrent.getLocations().clone();
			ArrayList<String> cloneTrackings = (ArrayList<String>)itemCurrent.getListTrackings().clone();
			itemCloned.setLocations(cloneLocations);
			itemCloned.setListTrackings(cloneTrackings);
			listItemsWhLO.add(itemCloned);
		}catch(Exception e){
			System.err.println("error cloning item in cloneItemToListWhLo ");
		}
	}
	

	/**
	 * @return the id
	 */
	public String getIdPODepartment() {
		return idPODepartment;
	}


	/**
	 * @return the idno
	 */
	public String getIdno() {
		return idno;
	}

	/**
	 * @param idno the idno to set
	 */
	public void setIdno(String idno) {
		this.idno = idno;
	}

	/**
	 * @param id the id to set
	 */
	public void setIdPODepartment(String id) {
		this.idPODepartment = id;
	}


	/**
	 * @return the po
	 */
	public String getPo() {
		return po;
	}


	/**
	 * @param po the po to set
	 */
	public void setPo(String po) {
		this.po = po;
	}


	/**
	 * @return the rep
	 */
	public String getRep() {
		return rep;
	}


	/**
	 * @param rep the rep to set
	 */
	public void setRep(String rep) {
		this.rep = rep;
	}


	/**
	 * @return the idClient
	 */
	public int getIdClient() {
		return idClient;
	}


	/**
	 * @param idClient the idClient to set
	 */
	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}


	/**
	 * @return the nameClient
	 */
	public String getNameClient() {
		return nameClient;
	}


	/**
	 * @param nameClient the nameClient to set
	 */
	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
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
	 * @return the nameSupplier
	 */
	public String getNameSupplier() {
		return nameSupplier;
	}


	/**
	 * @param nameSupplier the nameSupplier to set
	 */
	public void setNameSupplier(String nameSupplier) {
		this.nameSupplier = nameSupplier;
	}


	/**
	 * @return the dep
	 */
	public String getDep() {
		return dep;
	}

	/**
	 * @return the idDep
	 */
	public int getIdDep() {
		return idDep;
	}

	/**
	 * @param idDep the idDep to set
	 */
	public void setIdDep(int idDep) {
		this.idDep = idDep;
	}

	/**
	 * @param dep the dep to set
	 */
	public void setDep(String dep) {
		this.dep = dep;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}


	/**
	 * @return the nameStatus
	 */
	public String getNameStatus() {
		return nameStatus;
	}


	/**
	 * @param nameStatus the nameStatus to set
	 */
	public void setNameStatus(String nameStatus) {
		this.nameStatus = nameStatus;
	}


	/**
	 * @return the idEmployeeEntered
	 */
	public String getIdEmployeeEntered() {
		return idEmployeeEntered;
	}


	/**
	 * @param idEmployeeEntered the idEmployeeEntered to set
	 */
	public void setIdEmployeeEntered(String idEmployeeEntered) {
		this.idEmployeeEntered = idEmployeeEntered;
	}


	/**
	 * @return the nameEmployeeEntered
	 */
	public String getNameEmployeeEntered() {
		return nameEmployeeEntered;
	}


	/**
	 * @param nameEmployeeEntered the nameEmployeeEntered to set
	 */
	public void setNameEmployeeEntered(String nameEmployeeEntered) {
		this.nameEmployeeEntered = nameEmployeeEntered;
	}


	/**
	 * @return the listItemsWhLO
	 */
	public ArrayList<ModelItemWhReceipt> getListItemsWhLO() {
		return listItemsWhLO;
	}


	/**
	 * @param listItemsWhLO the listItemsWhLO to set
	 */
	public void setListItemsWhLO(ArrayList<ModelItemWhReceipt> listItemsWhLO) {
		this.listItemsWhLO = listItemsWhLO;
	}


	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}


	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	/**
	 * @return the truckCompany
	 */
	public ModelCarrier getTruckCompany() {
		return truckCompany;
	}

	/**
	 * @param truckCompany the truckCompany to set
	 */
	public void setTruckCompany(ModelCarrier truckCompany) {
		this.truckCompany = truckCompany;
	}

	/**
	 * @return the htPallets
	 */
	public int getHtPallets() {
		return htPallets;
	}

	/**
	 * @param htPallets the htPallets to set
	 */
	public void setHtPallets(int htPallets) {
		this.htPallets = htPallets;
	}

	/**
	 * @return the dateTimeReceived
	 */
	public String getDateTimeReceived() {
		return dateTimeReceived;
	}

	/**
	 * @param dateTimeReceived the dateTimeReceived to set
	 */
	public void setDateTimeReceived(String dateTimeReceived) {
		this.dateTimeReceived = dateTimeReceived;
	}

	/**
	 * @return the idWhReceipt
	 */
	public String getIdWhReceipt() {
		return idWhReceipt;
	}

	/**
	 * @param idWhReceipt the idWhReceipt to set
	 */
	public void setIdWhReceipt(String idWhReceipt) {
		this.idWhReceipt = idWhReceipt;
	}

	/**
	 * @return the listPhotosWh
	 */
	public ArrayList<ModelFileUpload> getListPhotosWh() {
		return listPhotosWh;
	}

	/**
	 * @param listPhotosWh the listPhotosWh to set
	 */
	public void setListPhotosWh(ArrayList<ModelFileUpload> listPhotosWh) {
		this.listPhotosWh = listPhotosWh;
	}

	/**
	 * @return the listDocswh
	 */
	public ArrayList<ModelFileUpload> getListDocsWh() {
		return listDocsWh;
	}

	/**
	 * @param listDocswh the listDocswh to set
	 */
	public void setListDocsWh(ArrayList<ModelFileUpload> listDocsWh) {
		this.listDocsWh = listDocsWh;
	}

	/**
	 * @return the numLabels
	 */
	public int getNumLabels() {
		return numLabels;
	}

	/**
	 * @param numLabels the numLabels to set
	 */
	public void setNumLabels(int numLabels) {
		this.numLabels = numLabels;
	}

	/**
	 * @return the urlLabelBase
	 */
	public String getUrlLabelBase() {
		return urlLabelBase;
	}

	/**
	 * @param urlLabelBase the urlLabelBase to set
	 */
	public void setUrlLabelBase(String urlLabelBase) {
		this.urlLabelBase = urlLabelBase;
	}
	
	/**
	 * @return the listItemsIndustrialPurchase
	 */
	public ArrayList<ModelItemIndustrialPurchase> getListItemsIndustrialPurchase() {
		return listItemsIndustrialPurchase;
	}

	/**
	 * @param listItemsIndustrialPurchase the listItemsIndustrialPurchase to set
	 */
	public void setListItemsIndustrialPurchase(
			ArrayList<ModelItemIndustrialPurchase> listItemsIndustrialPurchase) {
		this.listItemsIndustrialPurchase = listItemsIndustrialPurchase;
	}

	/**
	 * @return the isSavedIP
	 */
	public boolean isSavedIP() {
		return isSavedIP;
	}

	/**
	 * @param isSavedIP the isSavedIP to set
	 */
	public void setSavedIP(boolean isSavedIP) {
		this.isSavedIP = isSavedIP;
	}

	/**
	 * @return the listItemsRawMaterials
	 */
	public ArrayList<ModelItemRawMaterials> getListItemsRawMaterials() {
		return listItemsRawMaterials;
	}

	/**
	 * @param listItemsRawMaterials the listItemsRawMaterials to set
	 */
	public void setListItemsRawMaterials(ArrayList<ModelItemRawMaterials> listItemsRawMaterials) {
		this.listItemsRawMaterials = listItemsRawMaterials;
	}

	/**
	 * @return the isSavedRm
	 */
	public boolean isSavedRM() {
		return isSavedRM;
	}

	/**
	 * @param isSavedRm the isSavedRm to set
	 */
	public void setSavedRM(boolean isSavedRM) {
		this.isSavedRM = isSavedRM;
	}

}
