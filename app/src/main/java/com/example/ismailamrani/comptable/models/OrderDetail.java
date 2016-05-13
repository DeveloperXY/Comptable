package com.example.ismailamrani.comptable.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 13/05/2016.
 */
public class OrderDetail {
    private int productID;
    private int quantity;
    private double priceTTC;
    private String productName;
    private String productImage;

    public OrderDetail(JSONObject object) {
        try {
            productID = object.getInt("productID");
            quantity = object.getInt("quantity");
            priceTTC = object.getDouble("priceTTC");
            productName = object.getString("productName");
            productImage = object.getString("productImage");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<OrderDetail> parseSuppliers(JSONArray array) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject orderDetail = array.getJSONObject(i);
                orderDetails.add(new OrderDetail(orderDetail));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return orderDetails;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceTTC() {
        return priceTTC;
    }

    public void setPriceTTC(double priceTTC) {
        this.priceTTC = priceTTC;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
