package com.example.ismailamrani.comptable.ui.base;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ismailamrani.comptable.app.OGApplication;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mohammed Aouf ZOUAG on 06/05/2016.
 *
 * An abstract activity class which is able to send HTTP requests.
 */
public abstract class HTTPActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "success";

    private OkHttpClient client;

    /**
     * @param url target destination
     * @param data request's parameters
     * @param method GET/POST
     * @param listener on the outgoing HTTP request
     */
    protected void sendHTTPRequest(String url,
                                   JSONObject data,
                                   Method method,
                                   RequestListener listener) {
        if (client == null)
            client = ((OGApplication) getApplication()).getOkHttpInstance();

        Request request = PhpAPI.createHTTPRequest(data, url, method);
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        listener.onRequestFailed();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        Log.i("RESPONSE", res);
                        try {
                            JSONObject obj = new JSONObject(res);
                            int resp = obj.getInt(KEY_STATUS);

                            listener.onRequestSucceeded(obj, resp);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
