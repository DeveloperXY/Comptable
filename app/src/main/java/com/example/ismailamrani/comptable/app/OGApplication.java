package com.example.ismailamrani.comptable.app;

import android.app.Application;

import okhttp3.OkHttpClient;

/**
 * Created by Mohammed Aouf ZOUAG on 06/05/2016.
 */
public class OGApplication extends Application {
    /**
     * Singleton instance.
     */
    private OkHttpClient client;

    public OkHttpClient getOkHttpInstance() {
        if (client == null) {
            synchronized (OGApplication.class) {
                if (client == null)
                    client = new OkHttpClient();
            }
        }

        return client;
    }
}
