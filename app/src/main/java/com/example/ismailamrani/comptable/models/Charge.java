package com.example.ismailamrani.comptable.models;

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
}
