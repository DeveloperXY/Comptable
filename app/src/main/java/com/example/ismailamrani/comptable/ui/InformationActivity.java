package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
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
 * Created by Redouane on 04/04/2016.
 */
public class InformationActivity extends ColoredStatusBarActivity {

    ImageView imageInformation;
    RelativeLayout fermer, removeClient, editClient;
    String id;
    TextView Nom, Adresse, Tel;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_client);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        context = this;
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .7));
        removeClient = (RelativeLayout) findViewById(R.id.removeClient);
        editClient = (RelativeLayout) findViewById(R.id.editClient);
        fermer = (RelativeLayout) findViewById(R.id.fermerlay);

        Nom = (TextView) findViewById(R.id.nameClient);
        Adresse = (TextView) findViewById(R.id.adresseClient);
        Tel = (TextView) findViewById(R.id.telClient);
        imageInformation = (ImageView) findViewById(R.id.ImageProfilinformation);
        Picasso.with(this).load(PhpAPI.IpBackend + "clients/client.png").transform(new CropCircleTransformation()).into(imageInformation);

        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent i = getIntent();
        id = i.getExtras().getString("id");
        System.out.println(">>>>>>>>>>>> ID : " + id);
        new getProduitbyId().execute(PhpAPI.getClientById);

    }

    //
    private class getProduitbyId extends AsyncTask<String, Void, String> {

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
                            // Picasso.with(getApplicationContext()).load(URLs.IpBackend+"produits/"+ usr.getString("photo")).into(Image);
                            Nom.setText(usr.getString("nom"));
                            Tel.setText(usr.getString("tel"));
                            Adresse.setText(usr.getString("adresse"));

                            // final String idc=usr.getString("idclient");
                            removeClient.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //  System.out.println(">>>>>>>>>> Remove Client "+idc);
                                    new supprimer().execute(PhpAPI.removeClient);
                                }
                            });

                            editClient.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //  System.out.println(">>>>>>>>>> Remove Client "+idc);
                                    Intent i = new Intent(context, EditClientActivity.class);
                                    i.putExtra("id", id);
                                    context.startActivity(i);
                                }
                            });

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

    //************************************** remove client ************************
    private class supprimer extends AsyncTask<String, Void, String> {

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

                    Toast toast = Toast.makeText(getApplicationContext(), "Bien Supprimer", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                    Intent i = new Intent(context, ClientListActivity.class);
                    context.startActivity(i);
                } else if (resp == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "erreur de suppression !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
