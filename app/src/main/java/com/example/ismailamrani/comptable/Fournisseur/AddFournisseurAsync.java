package com.example.ismailamrani.comptable.fournisseur;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

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
 * Created by Redouane on 22/03/2016.
 */
public class AddFournisseurAsync extends Activity {

    EditText nom, tel, adresse, fix, fax, email;
    TextView ajouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_fournisseur_layout);
        nom = (EditText) findViewById(R.id.nom_fournisseur);
        tel = (EditText) findViewById(R.id.tel_fournisseur);
        adresse = (EditText) findViewById(R.id.adresse_fournisseur);
        fix = (EditText) findViewById(R.id.fix_fournisseur);
        fax = (EditText) findViewById(R.id.fax_fournissuer);
        email = (EditText) findViewById(R.id.email_fournisseur);
        ajouter = (TextView) findViewById(R.id.ajouterfournisseur);

        ajouter.setOnClickListener(v -> {
            Supplier f = new Supplier();
            f.setNom(nom.getText().toString());
            f.setTel(tel.getText().toString());
            f.setAdresse(adresse.getText().toString());
            f.setFix(fix.getText().toString());
            f.setFax(fax.getText().toString());
            f.setEmail(email.getText().toString());
            f.setUrl(PhpAPI.addFournisseur);
            new addfournisseur().execute(f);

        });
    }


    private class addfournisseur extends AsyncTask<Supplier, Void, String> {

        @Override
        protected String doInBackground(Supplier... params) {

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
                Params.put("Nom", params[0].getNom());
                Params.put("Tel", params[0].getTel());
                Params.put("Adresse", params[0].getAdresse());
                Params.put("Fix", params[0].getFix());
                Params.put("Fax", params[0].getFax());
                Params.put("Email", params[0].getEmail());


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
