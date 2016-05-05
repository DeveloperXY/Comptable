package com.example.ismailamrani.comptable.models;

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
}
