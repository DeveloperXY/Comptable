package com.example.ismailamrani.comptable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.ismailamrani.comptable.Adapters.ProduitAdapter;
import com.example.ismailamrani.comptable.CustumItems.ColorStatutBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.LocalData.ToDelete.GetProduit;
import com.example.ismailamrani.comptable.LocalData.URLs;
import com.example.ismailamrani.comptable.Models.ProduitModel;
import com.example.ismailamrani.comptable.ServiceWeb.convertInputStreamToString;
import com.example.ismailamrani.comptable.ServiceWeb.getQuery;

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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class Produis extends Activity implements OGActionBarInterface {
    private static final String TAG = Produis.class.getSimpleName();

    OGActionBar MyActionBar;
    ListView List;

    ArrayList<ProduitModel> ListProduit = new ArrayList<>();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits);
        Log.d(TAG, TAG);

        context = this;

        new ColorStatutBar().ColorStatutBar(this);

        MyActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        MyActionBar.setActionBarListener(this);
        MyActionBar.setTitle("Produit");

        List = (ListView) findViewById(R.id.List);
        new GetData().execute(URLs.getProduit);

    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddProduit.class));
    }

    private class GetData extends AsyncTask<String, Void, String> {

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
                JSONArray listproduits = j.getJSONArray("produit");

                for (int i = 0; i < listproduits.length(); i++) {
                    JSONObject usr = listproduits.getJSONObject(i);
                    ProduitModel itm = new ProduitModel();
                    itm.setID(Integer.parseInt(usr.getString("idp")));
                    itm.setLibelle(usr.getString("libelle"));
                    itm.setPrixTTC(Double.parseDouble(usr.getString("prixTTC")));
                    itm.setQte(Integer.parseInt(usr.getString("qte")));
                    itm.setPhoto(URLs.IpBackend + "produits/" + usr.getString("photo"));

                    ListProduit.add(itm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ProduitAdapter adapter = new ProduitAdapter(context, ListProduit);
            List.setAdapter(adapter);


        }
    }

}
