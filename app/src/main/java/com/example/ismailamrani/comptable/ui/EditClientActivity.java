package com.example.ismailamrani.comptable.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.ClientModel;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.example.ismailamrani.comptable.webservice.convertInputStreamToString;
import com.example.ismailamrani.comptable.webservice.getQuery;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
 * Created by Redouane on 23/03/2016.
 */
public class EditClientActivity extends ColoredStatusBarActivity {
    EditText nomprenom, tel, adresse, email;
    TextView ajouter;

    ImageView ImageProfil;
    Context context;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_add);
        context = this;

        Intent i = getIntent();
        id = i.getExtras().getString("id");
        System.out.println(">>>>>>>>>>>> ID : " + id);
        new getClientbyId().execute(PhpAPI.getClientById);
        ImageProfil = (ImageView) findViewById(R.id.ImageProfil);

        Picasso.with(this).
                load(R.drawable.sergio)
                .transform(new CropCircleTransformation())
                .into(ImageProfil);

        nomprenom = (EditText) findViewById(R.id.nomcomletclient);
        tel = (EditText) findViewById(R.id.numtel);
        adresse = (EditText) findViewById(R.id.adresse);
        email = (EditText) findViewById(R.id.email);
        ajouter = (TextView) findViewById(R.id.enregistrerclient);

        ajouter.setOnClickListener(v -> {
            ClientModel clientItems = new ClientModel();
            clientItems.setNomPrenom(nomprenom.getText().toString());
            clientItems.setTel(tel.getText().toString());
            clientItems.setAdresse(adresse.getText().toString());
            clientItems.setEmail(email.getText().toString());
            clientItems.setUrl(PhpAPI.editClient);

            new addclient().execute(clientItems);
        });
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
                Params.put("ID", id);
                Params.put("NomPrenom", params[0].getNomPrenom());
                Params.put("Tel", params[0].getTel());
                Params.put("Adresse", params[0].getAdresse());
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
                    Toast toast = Toast.makeText(getApplicationContext(), "Bien Modifier", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                    startActivity(new Intent(context, ClientsActivity.class));

                } else if (resp == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "erreur  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //***************************** get client by ID ****************************
    private class getClientbyId extends AsyncTask<String, Void, String> {

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
                Params.put("ID", id);
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
                    try {
                        JSONObject o = new JSONObject(s);
                        JSONArray listproduits = o.getJSONArray("client");

                        for (int i = 0; i < listproduits.length(); i++) {
                            JSONObject usr = listproduits.getJSONObject(i);
                            Picasso.with(getApplicationContext()).load(PhpAPI.IpBackend + "clients/client.png").into(ImageProfil);

                            nomprenom.setText(usr.getString("nom"));
                            tel.setText(usr.getString("tel"));
                            adresse.setText(usr.getString("adresse"));
                            email.setText(usr.getString("email"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (resp == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Client Not Found  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
