package com.example.ismailamrani.comptable.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 05/05/2016.
 *
 * represents a sale or purchase order.
 */
public class Order {
    private int id;
    private String date;
    private int facture;
    private String factureID;
    private double totalPrice;

    public Order(int id, String date, int facture, String factureID, double totalPrice) {
        this.id = id;
        this.date = date;
        this.facture = facture;
        this.factureID = factureID;
        this.totalPrice = totalPrice;
    }

    public Order(JSONObject object) throws JSONException {
        this(
                object.getInt("id"),
                object.getString("date"),
                object.getInt("facture"),
                object.getString("factureID"),
                object.getDouble("total")
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFacture() {
        return facture;
    }

    public void setFacture(int facture) {
        this.facture = facture;
    }

    public String getFactureID() {
        return factureID;
    }

    public void setFactureID(String factureID) {
        this.factureID = factureID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static List<Order> parseOrders(JSONArray array) {
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject product = array.getJSONObject(i);
                orders.add(new Order(product));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return orders;
    }
}
