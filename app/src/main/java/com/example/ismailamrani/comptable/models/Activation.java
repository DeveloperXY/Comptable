package com.example.ismailamrani.comptable.models;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class Activation {
    private int id;
    private String code;
    private boolean activated;
    private int companyID;

    public Activation(int id, String code, boolean activated, int companyID) {
        this.id = id;
        this.code = code;
        this.activated = activated;
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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }
}
