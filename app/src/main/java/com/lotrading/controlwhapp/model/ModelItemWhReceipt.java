package com.lotrading.controlwhapp.model;

import java.util.ArrayList;


public class ModelItemWhReceipt implements Cloneable {
	
	private boolean hazmat;
	private int nPieces;
	private int idUnitType;
	private int positionUnitType;;
	private String nameUnitType;
	private double length;
	private double width;
	private double height;
	private double volume;
	private double weigthLB;
	private double weigthKG;
	private ArrayList<ModelLocationItemWh> listLocations;
	private ArrayList<String> listTrackings;
	private String remarks;
	private int poItem_id;
	
	//
	private boolean isCloned;
	private int relationIdRMItem;
	
	/**
	 * @param hazmat
	 * @param nPieces
	 * @param idUnitType
	 * @param nameUnitType
	 * @param length
	 * @param width
	 * @param height
	 * @param volume
	 * @param weigthLB
	 * @param weigthKG
	 * @param locations
	 * @param tracking
	 * @param remarks
	 */
	public ModelItemWhReceipt(boolean hazmat, int nPieces, int idUnitType, String nameUnitType, int positionTypeUnitItem, double length,
			double width, double height, double weigthLB, double weigthKG, ArrayList<ModelLocationItemWh> listLocations,
			ArrayList<String> listTrackings, String remarks) {
		this.hazmat = hazmat;
		this.nPieces = nPieces;
		this.idUnitType = idUnitType;
		this.positionUnitType = positionTypeUnitItem;
		this.nameUnitType = nameUnitType;
		this.length = length;
		this.width = width;
		this.height = height;
		this.volume = ((width*length*height)/1728);
		this.weigthLB = weigthLB;
		this.weigthKG = weigthKG;
		this.listLocations = listLocations;
		this.listTrackings  = listTrackings;
		this.remarks = remarks;
		this.setCloned(false);
		this.setRelationIdRMItem(-1);
	}
	public ModelItemWhReceipt(boolean hazmat, int nPieces, int idUnitType, String nameUnitType, int positionTypeUnitItem, double length,
			double width, double height, double weigthLB, double weigthKG, ArrayList<ModelLocationItemWh> listLocations,
			ArrayList<String> listTrackings, String remarks,int poItem_id) {
		this.hazmat = hazmat;
		this.nPieces = nPieces;
		this.idUnitType = idUnitType;
		this.positionUnitType = positionTypeUnitItem;
		this.nameUnitType = nameUnitType;
		this.length = length;
		this.width = width;
		this.height = height;
		this.volume = ((width*length*height)/1728);
		this.weigthLB = weigthLB;
		this.weigthKG = weigthKG;
		this.listLocations = listLocations;
		this.listTrackings  = listTrackings;
		this.remarks = remarks;
		this.setCloned(false);
		this.setRelationIdRMItem(-1);
		this.poItem_id = poItem_id;
	}


	public boolean isHazmat() {
		return hazmat;
	}

	public ArrayList<ModelLocationItemWh> getListLocations() {
		return listLocations;
	}

	public void setListLocations(ArrayList<ModelLocationItemWh> listLocations) {
		this.listLocations = listLocations;
	}

	public int getPoItem_id() {
		return poItem_id;
	}

	public void setPoItem_id(int poItem_id) {
		this.poItem_id = poItem_id;
	}

	@Override
	protected Object clone() {
		Object objCloned = null;
		try{
			objCloned = super.clone();
		}catch(CloneNotSupportedException ex){
			System.out.println("cloned is no supported");
		}
		return objCloned;
	}
	
	/**
	 * @return the hazmat
	 */
	public boolean getHazmat() {
		return hazmat;
	}
	/**
	 * @param hazmat the hazmat to set
	 */
	public void setHazmat(boolean hazmat) {
		this.hazmat = hazmat;
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
	 * @return the nameUnitType
	 */
	public String getNameUnitType() {
		return nameUnitType;
	}
	/**
	 * @param nameUnitType the nameUnitType to set
	 */
	public void setNameUnitType(String nameUnitType) {
		this.nameUnitType = nameUnitType;
	}
	/**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}
	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	/**
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}
	/**
	 * @return the weigthLB
	 */
	public double getWeigthLB() {
		return weigthLB;
	}
	/**
	 * @param weigthLB the weigthLB to set
	 */
	public void setWeigthLB(double weigthLB) {
		this.weigthLB = weigthLB;
	}
	/**
	 * @return the weigthKG
	 */
	public double getWeigthKG() {
		return weigthKG;
	}
	/**
	 * @param weigthKG the weigthKG to set
	 */
	public void setWeigthKG(double weigthKG) {
		this.weigthKG = weigthKG;
	}
	/**
	 * @return the locations
	 */
	public ArrayList<ModelLocationItemWh> getLocations() {
		return listLocations;
	}
	/**
	 * @param locations the locations to set
	 */
	public void setLocations(ArrayList<ModelLocationItemWh> locations) {
		this.listLocations = locations;
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
	 * @return the listTrackings
	 */
	public ArrayList<String> getListTrackings() {
		return listTrackings;
	}

	/**
	 * @param listTrackings the listTrackings to set
	 */
	public void setListTrackings(ArrayList<String> listTrackings) {
		this.listTrackings = listTrackings;
	}


	/**
	 * @return the isCloned
	 */
	public boolean isCloned() {
		return isCloned;
	}


	/**
	 * @param isCloned the isCloned to set
	 */
	public void setCloned(boolean isCloned) {
		this.isCloned = isCloned;
	}


	/**
	 * @return the relationIdRMItem
	 */
	public int getRelationIdRMItem() {
		return relationIdRMItem;
	}


	/**
	 * @param relationIdRMItem the relationIdRMItem to set
	 */
	public void setRelationIdRMItem(int relationIdRMItem) {
		this.relationIdRMItem = relationIdRMItem;
	}
	
}
