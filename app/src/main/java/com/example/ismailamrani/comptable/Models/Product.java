package com.example.ismailamrani.comptable.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class Product {

    private int ID;
    private String Libelle;
    private Double PrixHT;
    private Double PrixTTC;
    private String CodeBarre;
    private String Photo;
    private int Qte;
    private int Locale_ID;
    private String url;

    /**
     * Overloaded constructor.
     * Creates a new Product model based on the passed-in JSON object
     *
     * @param object to be parsed.
     */
    public Product(JSONObject object) throws JSONException {
        this(
                object.getInt("idp"),
                object.getString("libelle"),
                object.getDouble("prixHT"),
                object.getDouble("prixTTC"),
                object.getString("codeBar"),
                object.getString("photo"),
                object.getInt("qte"),
                object.getInt("local"),
                ""
        );
    }

    public Product(int ID, String libelle, Double prixHT, Double prixTTC,
                   String codeBarre, String photo, int qte, int locale_ID, String url) {
        this.ID = ID;
        Libelle = libelle;
        PrixHT = prixHT;
        PrixTTC = prixTTC;
        CodeBarre = codeBarre;
        Photo = photo;
        Qte = qte;
        Locale_ID = locale_ID;
        this.url = url;
    }

    public Product() {
    }

    /**
     * @param array of products in JSON format
     * @return list of parsed products
     */
    public static List<Product> parseProducts(JSONArray array) {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject product = array.getJSONObject(i);
                products.add(new Product(product));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return products;
    }


    /**
     * @return a JSON object representing this Product object.
     */
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("Libelle", Libelle);
            object.put("PrixHT", PrixHT);
            object.put("PrixTTC", PrixTTC);
            object.put("CodeBar", CodeBarre);
            object.put("Qte", Qte);
            object.put("Photo", Photo);
            object.put("Local", Locale_ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLibelle() {
        return Libelle;
    }

    public void setLibelle(String libelle) {
        Libelle = libelle;
    }

    public Double getPrixHT() {
        return PrixHT;
    }

    public void setPrixHT(Double prixHT) {
        PrixHT = prixHT;
    }

    public Double getPrixTTC() {
        return PrixTTC;
    }

    public void setPrixTTC(Double prixTTC) {
        PrixTTC = prixTTC;
    }

    public String getCodeBarre() {
        return CodeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        CodeBarre = codeBarre;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public int getQte() {
        return Qte;
    }

    public void setQte(int qte) {
        Qte = qte;
    }

    public int getLocale_ID() {
        return Locale_ID;
    }

    public void setLocale_ID(int locale_ID) {
        Locale_ID = locale_ID;
    }
}
