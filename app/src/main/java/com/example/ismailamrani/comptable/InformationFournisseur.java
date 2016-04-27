package com.example.ismailamrani.comptable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.LocalData.URLs;
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
public class InformationFournisseur extends Activity {

    ImageView imageInformation;
    RelativeLayout fermer;
    String id;
    TextView nameFournisseur,fournisseurAdresse,fournisseurTel;
    RelativeLayout editFournisseur,removeFournisseur,callFournisseur;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.information_fournisseur);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        context=this;
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .7));
        fermer = (RelativeLayout)findViewById(R.id.fermerlay);

        nameFournisseur=(TextView)findViewById(R.id.nameFournisseur);
        fournisseurAdresse=(TextView)findViewById(R.id.fournisseurAdresse);
        fournisseurTel=(TextView)findViewById(R.id.fournisseurTel);
        imageInformation = (ImageView)findViewById(R.id.ImageProfilinformation);

        removeFournisseur=(RelativeLayout)findViewById(R.id.removeFournisseur);
        editFournisseur=(RelativeLayout)findViewById(R.id.editFournisseur);
        callFournisseur=(RelativeLayout)findViewById(R.id.callFournisseur);
        Picasso.with(this).load(R.drawable.flogo).transform(new CropCircleTransformation()).into(imageInformation);

        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        id = i.getExtras().getString("id");
        System.out.println(">>>>>>>>>>>> ID : " + id);
        new getFopurnisseurByID().execute(URLs.getFournisseurByID);

    }


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
                            nameFournisseur.setText(usr.getString("nom"));
                            fournisseurTel.setText(usr.getString("tel"));
                            fournisseurAdresse.setText(usr.getString("adresse"));

                            final String call=usr.getString("tel");
                            // final String idc=usr.getString("idclient");
                            removeFournisseur.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new supprimer().execute(URLs.removeFournisseur);
                                }
                            });

                            editFournisseur.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(context, EditFournisseur.class);
                                    i.putExtra("id",id);
                                    context.startActivity(i);
                                }
                            });
                            callFournisseur.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   // String n = "tel:5556";
                                   // Intent intent = new Intent( Intent.ACTION_CALL, Uri.parse( n ) );
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + call));
                                   try{
                                       startActivity( callIntent );
                                   }catch(Exception e){
                                       System.out.println(" >>>>>>>>>>> "+e.getMessage());
                                   }

                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else if (resp == 0){

                    Toast toast = Toast.makeText(getApplicationContext(), "Client Not Found  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    //*************************** remove Fournisseur ******************
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
                Params.put("ID",id);
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

                    Toast toast = Toast.makeText(getApplicationContext(), "Bien Supprimer", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                    Intent i = new Intent(context,FournisseurList.class);
                    context.startActivity(i);
                }
                else if (resp == 0){
                    Toast toast = Toast.makeText(getApplicationContext(), "erreur de suppression !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
