package com.example.ismailamrani.comptable.utils.http;

import org.json.JSONObject;

/**
 * Created by Mohammed Aouf ZOUAG on 30/05/2016.
 *
 * A listener that handles successful requests, which assumes an always
 * present internet connection.
 */
public abstract class SuccessRequestListener implements RequestListener {
    @Override
    public final void onNetworkError() {

    }

    @Override
    public void onRequestFailed(int status, JSONObject response) {

    }
}
