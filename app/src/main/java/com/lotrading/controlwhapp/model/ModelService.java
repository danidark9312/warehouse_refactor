package com.lotrading.controlwhapp.model;


public class ModelService {
	
	private String service;
	private String [][] params;
	private String callback;
	private String loadingMessage;
	
	public ModelService() {
		service = "";
		params = null;
		loadingMessage = "Loading";
		callback = "";
	}
	
	public ModelService(String service, String[][] params, String callback) {
		this.service = service;
		this.params = params;
		this.callback = callback;
		this.loadingMessage = "Loading";
	}
	
	public ModelService(String service, String[][] params, String callback, String loadingMessage) {
		this.service = service;
		this.params = params;
		this.callback = callback;
		this.loadingMessage = loadingMessage;
	}
	
	public String[][] getParams() {
		return params;
	}
	
	public String getService() {
		return service;
	}
	
	public String getCallback() {
		return callback;
	}
	
	public void setService(String service) {
		this.service = service;
	}
	
	public void setParams(String[][] params) {
		this.params = params;
	}
	
	public void setCallback(String callback) {
		this.callback = callback;
	}
	
	public String getLoadingMessage() {
		return loadingMessage;
	}
	
	public void setLoadingMessage(String loadingMessage) {
		this.loadingMessage = loadingMessage;
	}
	
}
