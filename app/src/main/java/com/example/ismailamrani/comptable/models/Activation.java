package com.example.ismailamrani.comptable.models;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class Activation {
    private int id;
    private String code;
    private boolean status;
    private int companyID;

    public Activation(int id, String code, boolean status, int companyID) {
        this.id = id;
        this.code = code;
        this.status = status;
        this.companyID = companyID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }
}
