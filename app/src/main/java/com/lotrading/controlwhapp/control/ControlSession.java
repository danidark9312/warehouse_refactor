package com.lotrading.controlwhapp.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.lotrading.controlwhapp.model.ModelEmployee;

public class ControlSession {
	
	private final String FILEAUTH = "wc_auth";
	private boolean isSession;
	private ModelEmployee employeeSession;
	
	public ControlSession() {
		isSession = false;
		employeeSession = new ModelEmployee("0", "guest", "guest", "guest@lotrading.com", "GUEST", false);
	}
	
	public void loginIn(Activity activityContext, ModelEmployee employeeSession, JSONObject json){
		//rewriteLocalFileAuth(activityContext, json.toString());
		isSession = true;
		this.employeeSession = employeeSession;
	}
	
	public void loginOut(Activity activityContext){
		//rewriteLocalFileAuth(activityContext, ""); //empty strign, kill the file content
		isSession = false;
		employeeSession = null;
		employeeSession = new ModelEmployee("0", "guest", "guest", "guest@lotrading.com", "GUEST", false);
	}
	
	//save data in user's file, used for the future login
	private void rewriteLocalFileAuth(Activity activityContext, String data){
		try{
			FileOutputStream fos = activityContext.openFileOutput(FILEAUTH, Context.MODE_PRIVATE);
			fos.write(data.getBytes());
			fos.close();
		}catch(Exception e){
			Log.e("MainActivity", "Error in rewriteLocalFileAuth", e);
		}
	}
	
	//this used for get data user saved in the device
	public String readFileAuth(Activity activityContext){
		try{
			FileInputStream  fis = activityContext.openFileInput(FILEAUTH);
			
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader bufferedReader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}catch(Exception e){
			Log.i("MainActivity", "file is not created yet");
		}
		return "";
	}
	

	/**
	 * @return the isSession
	 */
	public boolean isSession() {
		return isSession;
	}

	/**
	 * @param isSession the isSession to set
	 */
	public void setSession(boolean isSession) {
		this.isSession = isSession;
	}

	/**
	 * @return the employeeSession
	 */
	public ModelEmployee getEmployeeSession() {
		return employeeSession;
	}

	/**
	 * @param employeeSession the employeeSession to set
	 */
	public void setEmployeeSession(ModelEmployee employeeSession) {
		this.employeeSession = employeeSession;
	}

	/**
	 * @return the fILEAUTH
	 */
	public String getFILEAUTH() {
		return FILEAUTH;
	}
	
	
}
