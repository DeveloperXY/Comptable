package com.example.ismailamrani.comptable.utils;

import org.json.JSONObject;

/**
 * Created by Mohammed Aouf ZOUAG on 06/05/2016.
 */
public interface RequestListener {
    void onRequestSucceeded(JSONObject response, int status);
    void onRequestFailed();
}
