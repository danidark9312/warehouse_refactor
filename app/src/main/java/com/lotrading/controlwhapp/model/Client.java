package com.lotrading.controlwhapp.model;


/**
 * Created by daniel on 26/12/2016.
 */

public class Client {

    private String codeClient;
    private String nameClient;

    private String idPartner;
    private String namePartner;
    private String codePartner;
    private String isClient;

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(String idPartner) {
        this.idPartner = idPartner;
    }

    public String getNamePartner() {
        return namePartner;
    }

    public void setNamePartner(String namePartner) {
        this.namePartner = namePartner;
    }

    public String getCodePartner() {
        return codePartner;
    }

    public void setCodePartner(String codePartner) {
        this.codePartner = codePartner;
    }

    public String getIsClient() {
        return isClient;
    }

    public void setIsClient(String isClient) {
        this.isClient = isClient;
    }
}
