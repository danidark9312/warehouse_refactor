package com.lotrading.controlwhapp.model;

public class ModelFileUpload {
	
	private String nameFile;
	private String pathFile;
	/**
	 * @param nameFile
	 * @param pathFile
	 */
	public ModelFileUpload(String nameFile, String pathFile) {
		super();
		this.setNameFile(nameFile);
		this.setPathFile(pathFile);
	}
	/**
	 * @return the nameFile
	 */
	public String getNameFile() {
		return nameFile;
	}
	/**
	 * @param nameFile the nameFile to set
	 */
	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}
	/**
	 * @return the pathFile
	 */
	public String getPathFile() {
		return pathFile;
	}
	/**
	 * @param pathFile the pathFile to set
	 */
	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}
	
	
	
}
