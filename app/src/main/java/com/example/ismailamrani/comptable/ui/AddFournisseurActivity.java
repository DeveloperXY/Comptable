package com.example.ismailamrani.comptable.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.example.ismailamrani.comptable.webservice.convertInputStreamToString;
import com.example.ismailamrani.comptable.webservice.getQuery;
import com.squareup.picasso.Picasso;

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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Redouane on 08/04/2016.
 */
public class AddFournisseurActivity extends ColoredStatusBarActivity {

    EditText nom, tel, fax, gsm, adresse, email;
    ImageView ImageProfil;
    LinearLayout addFournisseur;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fournisseur_add);

        context = this;
        nom = (EditText) findViewById(R.id.nomcomletclient);
        tel = (EditText) findViewById(R.id.numerofixFour);
        fax = (EditText) findViewById(R.id.numerofaxfour);
        gsm = (EditText) findViewById(R.id.numtelfour);
        adresse = (EditText) findViewById(R.id.adressefour);
        email = (EditText) findViewById(R.id.emailfour);
        ImageProfil = (ImageView) findViewById(R.id.ImageProfil);
        addFournisseur = (LinearLayout) findViewById(R.id.addFournisseur);

        Picasso.with(this).load(R.drawable.flogo).transform(new CropCircleTransformation()).into(ImageProfil);

        addFournisseur.setOnClickListener(v -> {
            Supplier f = new Supplier();
            f.setNom(nom.getText().toString());
            f.setTel(gsm.getText().toString());
            f.setFax(fax.getText().toString());
            f.setFix(tel.getText().toString());
            f.setAdresse(adresse.getText().toString());
            f.setEmail(email.getText().toString());
            f.setUrl(PhpAPI.addFournisseur);

            new addFournisseur().execute(f);
        });
    }


    //*********************** add Fournisseur *****************
    private class addFournisseur extends AsyncTask<Supplier, Void, String> {

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
                    finish();
                    startActivity(new Intent(context, FournisseurListActivity.class));

                } else if (resp == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "erreur  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
