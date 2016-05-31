package com.example.ismailamrani.comptable.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Redouane on 22/03/2016.
 */
public class Supplier implements Item, Parcelable {
    String id;
    String nom;
    String tel;
    String adresse;
    String fix;
    String fax;
    String email;
    String image;
    String url;

    public Supplier() {
    }

    public Supplier(String id, String nom, String tel, String adresse, String fix, String fax, String email, String image, String url) {
        this.id = id;
        this.nom = nom;
        this.tel = tel;
        this.adresse = adresse;
        this.fix = fix;
        this.fax = fax;
        this.email = email;
        this.image = image;
        this.url = url;
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

    private Supplier(Parcel in) {
        id = in.readString();
        nom = in.readString();
        tel = in.readString();
        adresse = in.readString();
        fix = in.readString();
        fax = in.readString();
        email = in.readString();
        image = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nom);
        dest.writeString(tel);
        dest.writeString(adresse);
        dest.writeString(fix);
        dest.writeString(fax);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<Supplier> CREATOR = new Parcelable.Creator<Supplier>() {

        @Override
        public Supplier createFromParcel(Parcel source) {
            return new Supplier(source);
        }

        @Override
        public Supplier[] newArray(int size) {
            return new Supplier[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supplier supplier = (Supplier) o;

        if (id != null ? !id.equals(supplier.id) : supplier.id != null) return false;
        if (nom != null ? !nom.equals(supplier.nom) : supplier.nom != null) return false;
        if (tel != null ? !tel.equals(supplier.tel) : supplier.tel != null) return false;
        if (adresse != null ? !adresse.equals(supplier.adresse) : supplier.adresse != null)
            return false;
        if (fix != null ? !fix.equals(supplier.fix) : supplier.fix != null) return false;
        if (fax != null ? !fax.equals(supplier.fax) : supplier.fax != null) return false;
        if (email != null ? !email.equals(supplier.email) : supplier.email != null) return false;
        if (image != null ? !image.equals(supplier.image) : supplier.image != null) return false;
        return url != null ? url.equals(supplier.url) : supplier.url == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (tel != null ? tel.hashCode() : 0);
        result = 31 * result + (adresse != null ? adresse.hashCode() : 0);
        result = 31 * result + (fix != null ? fix.hashCode() : 0);
        result = 31 * result + (fax != null ? fax.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
