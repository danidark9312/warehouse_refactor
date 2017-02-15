package com.lotrading.controlwhapp.model;



public class UploadImageResponse{

	private boolean upload;
	private String description;
	
	
	public boolean isUpload() {
		return upload;
	}
	public void setUpload(boolean upload) {
		this.upload = upload;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
