package com.example.ismailamrani.comptable.utils;

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
}
