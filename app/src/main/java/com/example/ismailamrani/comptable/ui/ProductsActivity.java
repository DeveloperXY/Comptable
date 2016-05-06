package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ProduitAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.example.ismailamrani.comptable.webservice.convertInputStreamToString;
import com.example.ismailamrani.comptable.webservice.getQuery;

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
public class ProductsActivity extends ColoredStatusBarActivity {

    OGActionBar MyActionBar;
    ListView List;
    ArrayList<Product> ListProduit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits);

        setupActionBar();

        List = (ListView) findViewById(R.id.List);
        new GetData().execute(PhpAPI.getProduit);
    }

    private void setupActionBar() {
        MyActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        MyActionBar.setActionBarListener(this);
        MyActionBar.setTitle("Produit");
    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddProductActivity.class));
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
                    Product itm = new Product();
                    itm.setID(Integer.parseInt(usr.getString("idp")));
                    itm.setLibelle(usr.getString("libelle"));
                    itm.setPrixTTC(Double.parseDouble(usr.getString("prixTTC")));
                    itm.setQte(Integer.parseInt(usr.getString("qte")));
                    itm.setPhoto(PhpAPI.IpBackend + "produits/" + usr.getString("photo"));

                    ListProduit.add(itm);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ProduitAdapter adapter = new ProduitAdapter(ProductsActivity.this, ListProduit);
            List.setAdapter(adapter);
        }
    }
}
