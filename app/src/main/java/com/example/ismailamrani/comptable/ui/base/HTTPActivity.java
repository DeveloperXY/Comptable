package com.example.ismailamrani.comptable.ui.base;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.ismailamrani.comptable.app.OGApplication;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    protected void sendHTTPRequest(String url,
                                   List<Pair<String, String>> params,
                                   Method method,
                                   RequestListener listener) {
        Request request = createHTTPRequest(params, url, method);
        sendRequest(request, listener);
    }

    protected void sendHTTPRequest(String url,
                                   Map<String, String> params,
                                   Method method,
                                   RequestListener listener) {
        Request request = createHTTPRequest(params, url, method);
        sendRequest(request, listener);
    }

    protected void sendHTTPRequest(String url,
                                   Method method,
                                   RequestListener listener) {
        sendHTTPRequest(url, new JSONObject(), method, listener);
    }

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
        Request request = createHTTPRequest(data, url, method);
        sendRequest(request, listener);
    }

    private void sendRequest(Request request, final RequestListener listener) {
        if (client == null)
            client = ((OGApplication) getApplication()).getOkHttpInstance();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        listener.onNetworkError();
                        runOnUiThread(() -> Toast.makeText(HTTPActivity.this,
                                "Network error.", Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        Log.i("RESPONSE", res);
                        try {
                            JSONObject obj = new JSONObject(res);
                            int status = obj.getInt(KEY_STATUS);

                            if (status == 1)
                                listener.onRequestSucceeded(obj);
                            else {
                                Log.i("RESPONSE", String.format(
                                        "Possible developer error:\n- Status code: %d\n- Response: %s",
                                        status, res));
                                listener.onRequestFailed(status, obj);
                            }

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

    private Request createHTTPRequest(List<Pair<String, String>> param, String url, Method method) {
        if (param == null)
            param = new ArrayList<>();

        if (method == Method.GET)
            return new Request.Builder()
                    .url(url + buildGETRequestParams(param))
                    .build();

        if (method == Method.POST) {
            FormBody.Builder builder = new FormBody.Builder();
            Iterator<Pair<String, String>> keys = param.iterator();

            while (keys.hasNext()) {
                Pair<String, String> pair = keys.next();
                String key = pair.first;
                builder.add(key, pair.second);
            }
            RequestBody requestBody = builder.build();

            return new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }

        return null;
    }

    private Request createHTTPRequest(Map<String, String> param, String url, Method method) {
        if (param == null)
            param = new HashMap<>();

        if (method == Method.GET)
            return new Request.Builder()
                    .url(url + buildGETRequestParams(param))
                    .build();

        if (method == Method.POST) {
            FormBody.Builder builder = new FormBody.Builder();
            Iterator<Map.Entry<String, String>> iterator = param.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.add(entry.getKey(), entry.getValue());
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
    private String buildGETRequestParams(JSONObject params) {
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

    /**
     * @param params pairs of string request parameters
     * @return ?key=value&
     */
    private String buildGETRequestParams(List<Pair<String, String>> params) {
        StringBuilder sb = new StringBuilder("?");

        Iterator<Pair<String, String>> keys = params.iterator();
        while (keys.hasNext()) {
            Pair<String, String> pair = keys.next();
            String key = pair.first;
            sb.append(key)
                    .append("=")
                    .append(pair.second);

            if (keys.hasNext())
                sb.append("&");
        }

        return sb.toString();
    }

    private String buildGETRequestParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder("?");

        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            sb.append(key)
                    .append("=")
                    .append(entry.getValue());

            if (iterator.hasNext())
                sb.append("&");
        }

        return sb.toString();
    }
}
