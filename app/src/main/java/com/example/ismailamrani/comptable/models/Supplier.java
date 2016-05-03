package com.example.ismailamrani.comptable.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Redouane on 22/03/2016.
 */
public class Supplier implements Item {
    String id;
    String nom;
    String tel;
    String adresse;
    String fix;
    String fax;
    String email;

    public Supplier() {
    }

    public Supplier(JSONObject object) throws JSONException {
        this.id = object.getString("idfournisseur");
        this.nom = object.getString("nom");
        this.tel = object.getString("tel");
        this.adresse = object.getString("adresse");
        this.fix = object.getString("fix");
        this.fax = object.getString("fax");
        this.email = object.getString("email");
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getFix() {
        return fix;
    }

    public void setFix(String fix) {
        this.fix = fix;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getLabel() {
        return nom;
    }

    /**
     * @param array of products in JSON format
     * @return list of parsed suppliers
     */
    public static List<Supplier> parseSuppliers(JSONArray array) {
        List<Supplier> suppliers = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject supplier = array.getJSONObject(i);
                suppliers.add(new Supplier(supplier));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return suppliers;
    }
}
