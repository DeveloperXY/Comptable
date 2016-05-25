package com.example.ismailamrani.comptable.ui.base;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ismailamrani.comptable.app.OGApplication;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Mohammed Aouf ZOUAG on 06/05/2016.
 * <p>
 * An abstract activity class which is able to send HTTP requests & inform
 * corresponding listeners.
 */
public abstract class HTTPActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "success";

    private OkHttpClient client;

    /**
     * @param url      target destination
     * @param data     request's parameters
     * @param method   GET/POST
     * @param listener on the outgoing HTTP request
     */
    protected void sendHTTPRequest(String url,
                                   JSONObject data,
                                   Method method,
                                   RequestListener listener) {
        if (client == null)
            client = ((OGApplication) getApplication()).getOkHttpInstance();

        Request request = createHTTPRequest(data, url, method);
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

    /**
     * @param param  the request's body that contains the request's params to the server
     * @param url    target
     * @param method HTTP method (POST/GET/...)
     * @return a fully setup request
     */
    private Request createHTTPRequest(JSONObject param, String url, Method method) {
        if (param == null)
            param = new JSONObject();

        if (method == Method.GET)
            return new Request.Builder()
                    .url(url + buildGETRequestParams(param))
                    .build();

        if (method == Method.POST) {
            FormBody.Builder builder = new FormBody.Builder();
            Iterator<String> keys = param.keys();
            while (keys.hasNext()) {
                try {
                    String key = keys.next();
                    builder.add(key, param.getString(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            RequestBody requestBody = builder.build();

            return new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }

        return null;
    }

    /**
     * @param params JSONObject with the key/value params
     * @return ?key=value&
     */
    private static String buildGETRequestParams(JSONObject params) {
        StringBuilder sb = new StringBuilder("?");

        Iterator<String> keys = params.keys();
        while (keys.hasNext()) {
            try {
                String key = keys.next();
                sb.append(key)
                        .append("=")
                        .append(params.get(key));

                if (keys.hasNext())
                    sb.append("&");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
