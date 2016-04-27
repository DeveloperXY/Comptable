package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.LocalData.URLs;
import com.example.ismailamrani.comptable.Models.Fournisseur;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ServiceWeb.convertInputStreamToString;
import com.example.ismailamrani.comptable.ServiceWeb.getQuery;
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
 * Created by Redouane on 08/04/2016.
 */
public class EditFournisseurActivity extends Activity {
    private static final String TAG = EditFournisseurActivity.class.getSimpleName();

    EditText nom,tel,fax,gsm,adresse,email;
    ImageView ImageProfil;
    LinearLayout addFournisseur;
    Context context;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fournisseur_add);
        Log.d(TAG, TAG);


        Intent i = getIntent();
        id = i.getExtras().getString("id");
        System.out.println(">>>>>>>>>>>> ID : " + id);
        new getFopurnisseurByID().execute(URLs.getFournisseurByID);

        context=this;
        nom=(EditText)findViewById(R.id.nomcomletclient);
        tel=(EditText)findViewById(R.id.numerofixFour);
        fax=(EditText)findViewById(R.id.numerofaxfour);
        gsm=(EditText)findViewById(R.id.numtelfour);
        adresse=(EditText)findViewById(R.id.adressefour);
        email=(EditText)findViewById(R.id.emailfour);
        ImageProfil= (ImageView)findViewById(R.id.ImageProfil);
        addFournisseur=(LinearLayout)findViewById(R.id.addFournisseur);

        Picasso.with(this).load(R.drawable.flogo).transform(new CropCircleTransformation()).into(ImageProfil);

        addFournisseur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fournisseur f = new Fournisseur();
                f.setNom(nom.getText().toString());
                f.setTel(gsm.getText().toString());
                f.setFax(fax.getText().toString());
                f.setFix(tel.getText().toString());
                f.setAdresse(adresse.getText().toString());
                f.setEmail(email.getText().toString());
                f.setUrl(URLs.editFournisseur);


                new addFournisseur().execute(f);

            }
        });

    }

//******************************** get Fournisseur by ID **********************
private class getFopurnisseurByID extends AsyncTask<String, Void, String> {

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
            if (resp == 1){



                try {
                    JSONObject o = new JSONObject(s);
                    JSONArray listproduits = o.getJSONArray("fournisseur");

                    for (int i = 0; i < listproduits.length(); i++) {
                        JSONObject usr = listproduits.getJSONObject(i);
                        // Picasso.with(getApplicationContext()).load(URLs.IpBackend+"produits/"+ usr.getString("photo")).into(Image);
                        nom.setText(usr.getString("nom"));
                        gsm.setText(usr.getString("tel"));
                        adresse.setText(usr.getString("adresse"));
                        fax.setText(usr.getString("fax"));
                        tel.setText(usr.getString("fix"));
                        email.setText(usr.getString("email"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            else if (resp == 0){

                Toast toast = Toast.makeText(getApplicationContext(), "Fournisseur Not Found  !!!!", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

    //*********************** add Fournisseur *****************
    private class addFournisseur extends AsyncTask<Fournisseur, Void, String> {

        @Override
        protected String doInBackground(Fournisseur... params) {

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
                Params.put("Nom",params[0].getNom());
                Params.put("Tel",params[0].getTel());
                Params.put("Adresse",params[0].getAdresse());
                Params.put("Fix",params[0].getFix());
                Params.put("Fax",params[0].getFax());
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
