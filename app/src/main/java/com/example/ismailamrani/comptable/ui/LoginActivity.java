package com.example.ismailamrani.comptable.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ServiceWeb.PhpAPI;
import com.example.ismailamrani.comptable.ServiceWeb.convertInputStreamToString;
import com.example.ismailamrani.comptable.ServiceWeb.getQuery;
import com.example.ismailamrani.comptable.UsedMethodes.CalculateScreenSize;
import com.example.ismailamrani.comptable.utils.Method;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private OkHttpClient client = new OkHttpClient();

    LinearLayout Valider;
    EditText nom, motdepass;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG);

        new CalculateScreenSize().CalculateScreenSize(this);

        setContentView(R.layout.activity_splash);

        Valider = (LinearLayout) findViewById(R.id.Valider);
        nom = (EditText) findViewById(R.id.username);
        motdepass = (EditText) findViewById(R.id.mp);


        Valider.setOnClickListener(v -> {

        });
    }

    /**
     * Validates the user name & password.
     *
     * @param fields to validate
     * @return true if input is valid, false otherwise.
     */
    private boolean isInputValid(String... fields) {
        return false;
    }


    private class logintest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                URLConnection conn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Map<String, Object> Params = new LinkedHashMap<>();
                // Params.put("ID", id);
//                Params.put("Username", userr);
//                Params.put("Password", mpp);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(new getQuery().getQuery(Params));
                writer.flush();
                writer.close();
                os.close();
                httpConn.connect();
                InputStream is = httpConn.getInputStream();

                return new convertInputStreamToString().convertInputStreamToString(is);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);


            try {
                JSONObject j = new JSONObject(s);
                int resp = j.getInt("success");
                if (resp == 1) {


//                    startActivity(new Intent(context, AccueilActivity.class));


                } else if (resp == 0) {

                    //  Intent intent = new Intent(getApplicationContext(),ContactUs.class);
                    //  startActivity(intent);
                    Toast toast = Toast.makeText(getApplicationContext(), "can you register  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param url             destination URL
     * @param userCredentials the user's informations
     * @throws IOException
     */
    void postLogin(String url, JSONObject userCredentials) throws IOException {
        Request request = PhpAPI.createHTTPRequest(userCredentials, url, Method.POST);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this,
                                call.request().toString(), Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(res);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
