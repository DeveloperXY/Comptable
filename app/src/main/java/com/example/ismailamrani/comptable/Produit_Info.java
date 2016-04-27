package com.example.ismailamrani.comptable;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.CustumItems.ColorStatutBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBarInterface;
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

/**
 * Created by Ismail Amrani on 25/03/2016.
 */
public class Produit_Info extends Activity implements OGActionBarInterface {
    private static final String TAG = Produit_Info.class.getSimpleName();

    OGActionBar MyActionBar;
    ImageView Image;
    int id;

    TextView PrixHT, PrixTTC, Stock, Code, Libelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produit_info);
        Log.d(TAG, TAG);

        new ColorStatutBar().ColorStatutBar(this);

        MyActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        MyActionBar.setTitle("Sumsung Galaxy S6");
        MyActionBar.setActionBarListener(this);
        Image = (ImageView)findViewById(R.id.Imageaff);
        PrixHT = (TextView)findViewById(R.id.PrixHTaff);
        PrixTTC = (TextView)findViewById(R.id.PrixTTCaff);
        Code =(TextView)findViewById(R.id.Codeaff);
        Stock =(TextView)findViewById(R.id.Stockaff);
        Libelle =(TextView)findViewById(R.id.Libelleaff);



        Intent i = getIntent();
        id = i.getExtras().getInt("id");
        new getProduitbyId().execute(URLs.getProduitById);

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
                if (resp == 1){



                    try {
                        JSONObject o = new JSONObject(s);
                        JSONArray listproduits = o.getJSONArray("produit");

                        for (int i = 0; i < listproduits.length(); i++) {
                            JSONObject usr = listproduits.getJSONObject(i);
                            Picasso.with(getApplicationContext()).load(URLs.IpBackend+"produits/"+ usr.getString("photo")).into(Image);
                            Libelle.setText(usr.getString("libelle"));
                            PrixHT.setText(usr.getString("prixHT"));
                            PrixTTC.setText(usr.getString("prixTTC"));
                            Code.setText(usr.getString("codeBar"));
                            Stock.setText(usr.getString("qte"));


                            MyActionBar.setTitle(usr.getString("libelle"));



                            //itm.setPhoto(URLs.IpBackend + "produits/" + usr.getString("photo"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
                else if (resp == 0){

                    //  Intent intent = new Intent(getApplicationContext(),ContactUs.class);
                    //  startActivity(intent);
                    Toast toast = Toast.makeText(getApplicationContext(), "Produit Not Found  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddProduit.class));
    }
}
