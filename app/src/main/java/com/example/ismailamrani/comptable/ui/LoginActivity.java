package com.example.ismailamrani.comptable.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.ServiceWeb.PhpAPI;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ServiceWeb.convertInputStreamToString;
import com.example.ismailamrani.comptable.ServiceWeb.getQuery;
import com.example.ismailamrani.comptable.UsedMethodes.CalculateScreenSize;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    LinearLayout Valider;
    Context context;
    EditText nom,motdepass;
    String userr,mpp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Log.d(TAG, TAG);

        new CalculateScreenSize().CalculateScreenSize(this);

        setContentView(R.layout.activity_splash);

        Valider = (LinearLayout) findViewById(R.id.Valider);
        nom = (EditText)findViewById(R.id.username);
        motdepass = (EditText)findViewById(R.id.mp);


        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userr = nom.getText().toString();
                mpp = motdepass.getText().toString();
                if(userr.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "username is required", Toast.LENGTH_LONG);
                    toast.show();
                }else if(mpp.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "password is required", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    new logintest().execute(PhpAPI.login);
                }

            }
        });



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
                Params.put("Username",userr);
                Params.put("Password",mpp);
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
                if (resp == 1){



                    startActivity(new Intent(context, AccueilActivity.class));


                }
                else if (resp == 0){

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
}
