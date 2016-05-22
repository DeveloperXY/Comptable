package com.example.ismailamrani.comptable.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 21/05/2016.
 */
public class Charge {
    private int id;
    private String description;
    private double price;
    private String date;
    private int localeID;

    public Charge(int id, String description, double price, String date, int localeID) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.date = date;
        this.localeID = localeID;
    }

    public Charge(JSONObject object) throws JSONException {
        this(
                object.getInt("idcharge"),
                object.getString("description"),
                object.getDouble("prix"),
                object.getString("date"),
                object.getInt("local"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLocaleID() {
        return localeID;
    }

    public void setLocaleID(int localeID) {
        this.localeID = localeID;
    }

    public static List<Charge> parseCharges(JSONArray array) {
        List<Charge> charges = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject charge = array.getJSONObject(i);
                charges.add(new Charge(charge));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return charges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Charge charge = (Charge) o;

        if (id != charge.id) return false;
        if (Double.compare(charge.price, price) != 0) return false;
        if (localeID != charge.localeID) return false;
        if (description != null ? !description.equals(charge.description) : charge.description != null)
            return false;
        return date != null ? date.equals(charge.date) : charge.date == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + localeID;
        return result;
    }
}
