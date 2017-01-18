package com.lotrading.controlwhapp.model;

/**
 * Created by daniel on 23/12/2016.
 */

public class Po {


    private String poNumber;
    private String clientId;
    private String supplierId;
    private String tracking;


    private String id;
    private String idno;
    private String supplierName;
    private String supplierCode;
    private String clientName;
    private String clientCode;
    private String rep;
    private String poItemId;
    private int idDepartment;
    private String department;

    public String getDepartment() {
       return this.department;
    }

    public String getDepartamentoByCodigo(){
        if(this.department != null && this.department.trim() != ""){
            return department;
        }else{
            if(this.idDepartment!=0){
                switch (idDepartment){
                    case 2:
                        this.department = "RAW MATERIAL";
                        break;
                    case 3:
                        this.department = "INDUSTRIAL PURCHASE";
                        break;
                    case 4:
                        this.department = "LOGISTIC OPERATION";
                        break;
                }
                return department;
            }else{
                return null;
            }
        }
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(int idDepartment) {
        this.idDepartment = idDepartment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getPoItemId() {
        return poItemId;
    }

    public void setPoItemId(String poItemId) {
        this.poItemId = poItemId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }
}

