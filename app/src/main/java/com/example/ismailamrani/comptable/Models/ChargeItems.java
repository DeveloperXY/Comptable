package com.example.ismailamrani.comptable.models;

/**
 * Created by Redouane on 23/03/2016.
 */
public class ChargeItems {

    String Desc;
    String Prix;

    public String getLocal() {
        return Local;
    }

    public void setLocal(String local) {
        Local = local;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getPrix() {
        return Prix;
    }

    public void setPrix(String prix) {
        Prix = prix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String Local;
    String url;
}
