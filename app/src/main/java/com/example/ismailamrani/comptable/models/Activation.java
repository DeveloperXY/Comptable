package com.example.ismailamrani.comptable.models;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Activation(JSONObject data) {
        try {
            this.id = data.getInt("id");
            this.code = data.getString("code");
            this.activated = true;
            this.companyID = data.getInt("companyID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public String toString() {
        return "Activation{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", activated=" + activated +
                ", companyID=" + companyID +
                '}';
    }
}
