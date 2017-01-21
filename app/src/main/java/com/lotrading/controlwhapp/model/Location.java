package com.lotrading.controlwhapp.model;

import com.google.gson.annotations.SerializedName;

public class Location{

	@SerializedName("id")
	private String whReceiptItemLocationId;
	private String loc;
	private String pc;
	
	
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getPc() {
		return pc;
	}
	public void setPc(String pc) {
		this.pc = pc;
	}
	public String getWhReceiptItemLocationId() {
		return whReceiptItemLocationId;
	}
	public void setWhReceiptItemLocationId(String whReceiptItemLocationId) {
		this.whReceiptItemLocationId = whReceiptItemLocationId;
	}
}