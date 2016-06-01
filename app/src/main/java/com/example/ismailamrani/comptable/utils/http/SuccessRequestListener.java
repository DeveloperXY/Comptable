package com.example.ismailamrani.comptable.utils.http;

import org.json.JSONObject;

/**
 * Created by Mohammed Aouf ZOUAG on 30/05/2016.
 */
public abstract class SuccessRequestListener implements RequestListener {
    @Override
    public final void onNetworkError() {

    }

    @Override
    public void onRequestFailed(int status, JSONObject response) {

    }
}
