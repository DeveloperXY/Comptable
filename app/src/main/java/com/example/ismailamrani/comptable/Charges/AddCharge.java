package com.example.ismailamrani.comptable.Charges;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.ServiceWeb.PhpAPI;
import com.example.ismailamrani.comptable.Models.ChargeItems;
import com.example.ismailamrani.comptable.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Redouane on 23/03/2016.
 */
public class AddCharge extends Activity {

    EditText desc,prix,local;
    TextView ajouter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_charge_layout);
        desc = (EditText)findViewById(R.id.desc_charge);
        prix = (EditText)findViewById(R.id.prix_charge);
        local = (EditText)findViewById(R.id.local_charge);
        ajouter = (TextView)findViewById(R.id.ajoutercharge);

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChargeItems chargeItems = new ChargeItems();
                chargeItems.setDesc(desc.getText().toString());
                System.out.println(">>>> desc"+ chargeItems.getDesc());
                chargeItems.setPrix(prix.getText().toString());
                chargeItems.setLocal(local.getText().toString());
                chargeItems.setUrl(PhpAPI.addccharge);
                new addcharge().execute(chargeItems);
            }
        });
    }

    private class addcharge extends AsyncTask<ChargeItems, Void, String> {

        @Override
        protected String doInBackground(ChargeItems... params) {

            try {
                URL url = new URL(params[0].getUrl());
                URLConnection conn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Map<String, Object> Params = new LinkedHashMap<>();
                // Params.put("ID", id);

                Params.put("Description",params[0].getDesc());
                Params.put("Prix",params[0].getPrix());
                Params.put("Local",params[0].getLocal());



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


                    Toast toast = Toast.makeText(getApplicationContext(), "Bien Ajouter", Toast.LENGTH_LONG);
                    toast.show();


                } else if (resp == 0) {


                    Toast toast = Toast.makeText(getApplicationContext(), "erreur  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    public class convertInputStreamToString {
        public String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }
    }

    public class getQuery {
        public String getQuery(Map<String, Object> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            return result.toString();
        }
    }

}
