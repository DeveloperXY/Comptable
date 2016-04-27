package com.example.ismailamrani.comptable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.LocalData.RoundedImageView;
import com.example.ismailamrani.comptable.LocalData.URLs;
import com.example.ismailamrani.comptable.Models.ClientModel;
import com.example.ismailamrani.comptable.ServiceWeb.convertInputStreamToString;
import com.example.ismailamrani.comptable.ServiceWeb.getQuery;
import com.squareup.picasso.Picasso;


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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Redouane on 23/03/2016.
 */
public class AddClient extends Activity implements OGActionBarInterface {
    EditText nomprenom,tel,adresse,email;
    TextView ajouter;

    ImageView ImageProfil;
    OGActionBar MyActionBar;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_add);
        context = this;
       /* MyActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        MyActionBar.setActionBarListener(this);
        MyActionBar.setTitle("Ajouter Un client");
        MyActionBar.AddDisable();*/

        ImageProfil = (ImageView) findViewById(R.id.ImageProfil);

        Picasso.with(this).load(R.drawable.sergio).transform(new CropCircleTransformation()).into(ImageProfil);

        nomprenom = (EditText)findViewById(R.id.nomcomletclient);
        tel = (EditText)findViewById(R.id.numtel);
        adresse = (EditText)findViewById(R.id.adresse);
        email = (EditText)findViewById(R.id.email);
        ajouter = (TextView)findViewById(R.id.enregistrerclient);

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientModel clientItems = new ClientModel();
                clientItems.setNomPrenom(nomprenom.getText().toString());
                clientItems.setTel(tel.getText().toString());
                clientItems.setAdresse(adresse.getText().toString());
               clientItems.setEmail(email.getText().toString());
                clientItems.setUrl(URLs.addClient);

                new addclient().execute(clientItems);

            }
        });
    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }

    private class addclient extends AsyncTask<ClientModel, Void, String> {

        @Override
        protected String doInBackground(ClientModel... params) {

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
                Params.put("NomPrenom",params[0].getNomPrenom());
                Params.put("Tel",params[0].getTel());
                Params.put("Adresse",params[0].getAdresse());
                Params.put("Email",params[0].getEmail());

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
                    startActivity(new Intent(context, ClientList.class));

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
