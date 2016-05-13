package com.example.ismailamrani.comptable.utils;

import com.example.ismailamrani.comptable.models.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohammed Aouf ZOUAG on 04/05/2016.
 */
public class JSONUtils {
    /**
     * Assembles the passed-in JSONArray into a JSONObject, using the @tag argument.
     * @param array
     * @param tag
     * @return JSONObject
     */
    public static JSONObject bundleWithTag(JSONArray array, String tag) {
        JSONObject data = new JSONObject();
        try {
            data.put(tag, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * @param serial
     * @return a JSONObject containing the passed-in serial.
     */
    public static JSONObject bundleSerialToJSON(String serial) {
        JSONObject data = new JSONObject();

        try {
            data.put("serial", serial);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * @param order
     * @return a JSONObject containing the passed-in order ID.
     */
    public static JSONObject bundleOrderToJSON(Order order) {
        JSONObject data = new JSONObject();

        try {
            data.put("orderID", order.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static JSONObject bundleChargeIDToJSON(int chargeID) {
        JSONObject data = new JSONObject();

        try {
            data.put("factureID", chargeID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
