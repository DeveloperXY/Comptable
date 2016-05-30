package com.example.ismailamrani.comptable.utils.http;

import org.json.JSONObject;

/**
 * Created by Mohammed Aouf ZOUAG on 06/05/2016.
 */
public interface RequestListener {
    /**
     * This method is invoked when the success status code is equal to 1.
     * @param response sent back by the server, parsed as a JSONObject.
     */
    void onRequestSucceeded(JSONObject response);

    /**
     * Invoked when there is no internet connection.
     */
    void onNetworkError();
}
