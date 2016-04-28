package com.example.ismailamrani.comptable.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class ProduitModel {

    int ID;
    String Libelle;
    Double PrixHT;
    Double PrixTTC;
    String CodeBarre;
    String Photo;
    int Qte;
    int Locale_ID;
    String url;

    /**
     * Overloaded constructor.
     * Creates a new Product model based on the passed-in JSON object
     *
     * @param object to be parsed.
     */
    public ProduitModel(JSONObject object) throws JSONException {
        this(
                object.getInt("ID"),
                object.getString("Libelle"),
                object.getDouble("PrixHT"),
                object.getDouble("PrixTTC"),
                object.getString("CodeBar"),
                object.getString("Photo"),
                object.getInt("Qte"),
                object.getInt("Locale_ID"),
                ""
        );
    }

    public ProduitModel(int ID, String libelle, Double prixHT, Double prixTTC,
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
