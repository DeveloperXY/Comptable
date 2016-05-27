package com.example.ismailamrani.comptable.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 18/05/2016.
 */
public class Local {
    private int id;
    private String address;
    private String city;
    private String country;
    private String telephone;
    private String fix;
    private String fax;
    private String email;
    private String activite;
    private int companyID;

    public Local(int id, String address, String city, String country,
                 String telephone, String fix, String fax, String email,
                 String activite, int companyID) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.country = country;
        this.telephone = telephone;
        this.fix = fix;
        this.fax = fax;
        this.email = email;
        this.activite = activite;
        this.companyID = companyID;
    }

    public Local(JSONObject object) throws JSONException {
        this(
                object.getInt("ID"),
                object.getString("Adresse"),
                object.getString("Ville"),
                object.getString("Pays"),
                object.getString("Tel"),
                "",
                "",
                "",
                "",
                0
        );
    }

    public static List<Local> parseLocales(JSONArray array) {
        List<Local> locales = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject local = array.getJSONObject(i);
                locales.add(new Local(local));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return locales;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFix() {
        return fix;
    }

    public void setFix(String fix) {
        this.fix = fix;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    @Override
    public String toString() {
        return "Local{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", telephone='" + telephone + '\'' +
                ", fix='" + fix + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", activite='" + activite + '\'' +
                ", companyID=" + companyID +
                '}';
    }
}
