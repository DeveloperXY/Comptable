package com.example.ismailamrani.comptable.Models;

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
