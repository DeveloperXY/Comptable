package com.example.ismailamrani.comptable.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class Product implements Item, Parcelable {

    private int ID;
    private String Libelle;
    private Double PrixHT;
    private Double PrixTTC;
    private String CodeBarre;
    private String Photo;
    private int Qte;
    private int Locale_ID;
    private String url;
    private int supplier;

    public Product() {
        this(-1, "", 0d, 0d, "", "", 0, 0, "");
    }

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
                PhpAPI.IpBackend_IMAGES + object.getString("photo"),
                object.getInt("qte"),
                object.getInt("local"),
                "",
                -1
        );
    }

    public Product(int ID, String libelle, Double prixHT, Double prixTTC, String codeBarre,
                   String photo, int qte, int locale_ID, String url) {
        this(ID, libelle, prixHT, prixTTC, codeBarre, photo, qte, locale_ID, url, -1);
    }

    public Product(int ID, String libelle, Double prixHT, Double prixTTC, String codeBarre,
                   String photo, int qte, int locale_ID, String url, int supplier) {
        this.ID = ID;
        Libelle = libelle;
        PrixHT = prixHT;
        PrixTTC = prixTTC;
        CodeBarre = codeBarre;
        Photo = photo;
        Qte = qte;
        Locale_ID = locale_ID;
        this.url = url;
        this.supplier = supplier;
    }

    private Product(Parcel in) {
        ID = in.readInt();
        Libelle = in.readString();
        PrixHT = in.readDouble();
        PrixTTC = in.readDouble();
        CodeBarre = in.readString();
        Photo = in.readString();
        Qte = in.readInt();
        Locale_ID = in.readInt();
        url = in.readString();
        supplier = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Libelle);
        dest.writeDouble(PrixHT);
        dest.writeDouble(PrixTTC);
        dest.writeString(CodeBarre);
        dest.writeString(Photo);
        dest.writeInt(Qte);
        dest.writeInt(Locale_ID);
        dest.writeString(url);
        dest.writeInt(supplier);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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
            object.put("Supplier", supplier);

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

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    @Override
    public String getLabel() {
        return Libelle;
    }

    /**
     * @param products to be filtered
     * @param query    based upon the products will be filtered
     * @return the filtered list of products
     */
    public static List<Product> filter(List<Product> products, String query) {
        return Stream.of(products)
                .filter(product -> product.getLabel()
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (ID != product.ID) return false;
        if (Qte != product.Qte) return false;
        if (Locale_ID != product.Locale_ID) return false;
        if (supplier != product.supplier) return false;
        if (Libelle != null ? !Libelle.equals(product.Libelle) : product.Libelle != null)
            return false;
        if (PrixHT != null ? !PrixHT.equals(product.PrixHT) : product.PrixHT != null) return false;
        if (PrixTTC != null ? !PrixTTC.equals(product.PrixTTC) : product.PrixTTC != null)
            return false;
        if (CodeBarre != null ? !CodeBarre.equals(product.CodeBarre) : product.CodeBarre != null)
            return false;
        if (Photo != null ? !Photo.equals(product.Photo) : product.Photo != null) return false;
        return url != null ? url.equals(product.url) : product.url == null;

    }

    @Override
    public int hashCode() {
        int result = ID;
        result = 31 * result + (Libelle != null ? Libelle.hashCode() : 0);
        result = 31 * result + (PrixHT != null ? PrixHT.hashCode() : 0);
        result = 31 * result + (PrixTTC != null ? PrixTTC.hashCode() : 0);
        result = 31 * result + (CodeBarre != null ? CodeBarre.hashCode() : 0);
        result = 31 * result + (Photo != null ? Photo.hashCode() : 0);
        result = 31 * result + Qte;
        result = 31 * result + Locale_ID;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + supplier;
        return result;
    }
}
