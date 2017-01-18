package com.lotrading.controlwhapp.model;

import java.text.SimpleDateFormat;

import com.lotrading.controlwhapp.config.Config;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;


@SuppressLint("SimpleDateFormat")
public class ModelEmployee {
	
	private String id;
	private String name;
	private String lastname;
	private String email;
	private String login;
	private boolean isAuth;
	private Bitmap image;
	private String urlImage = "";
	private String dateTimeLogin;
	/**
	 * @param id
	 * @param name
	 * @param lastname
	 * @param email
	 * @param login
	 * @param isAyth
	 */
	public ModelEmployee(String id, String name, String lastname, String email, String login, boolean isAuth) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.email = email;
		this.login = login;
		this.isAuth = isAuth;
		image = null;
		urlImage = Config.SERVER_IP + "/ControlWarehouse/images/" + name + "-" + lastname + ".jpg";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		setDateTimeLogin(sdf.format(new java.util.Date()));  
	}
	
	public String getCompleteName() {
		return name + " " + lastname;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the isAuth
	 */
	public boolean isAuth() {
		return isAuth;
	}
	/**
	 * @param isAuth the isAuth to set
	 */
	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	public Bitmap getImage() {
		return image;
	}

	/**
	 * @return the urlImage
	 */
	public String getUrlImage() {
		return urlImage;
	}

	/**
	 * @param urlImage the urlImage to set
	 */
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	/**
	 * @return the dateTimeLogin
	 */
	public String getDateTimeLogin() {
		return dateTimeLogin;
	}

	/**
	 * @param dateTimeLogin the dateTimeLogin to set
	 */
	public void setDateTimeLogin(String dateTimeLogin) {
		this.dateTimeLogin = dateTimeLogin;
	}
	
}
