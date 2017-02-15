package com.lotrading.controlwhapp.model;

public class SaveWarehouseResponse{
	private boolean created;
	private Integer totalLabels;

	public boolean isCreated() {
		return created;
	}

	public void setCreated(boolean created) {
		this.created = created;
	}

	public Integer getTotalLabels() {
		return totalLabels;
	}
	public void setTotalLabels(Integer totalLabels) {
		this.totalLabels = totalLabels;
	}
}
