package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.FourniseurAdapter;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;
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

import butterknife.ButterKnife;

/**
 * Created by Redouane on 08/04/2016.
 */
public class FournisseurListActivity extends AnimatedActivity {

    ListView list;
    ArrayList<Supplier> ListF = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fournisseur);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
        list = (ListView) findViewById(R.id.Listfournisseur);
        new GetData().execute(PhpAPI.getFournisseur);
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Fournisseur");
    }

    @Override
    public void onAddPressed() {
        finish();
        startActivity(new Intent(this, AddFournisseurActivity.class));
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

                int resp = j.getInt("success");
                if (resp == 1) {
                    JSONArray listproduits = j.getJSONArray("fournisseur");

                    for (int i = 0; i < listproduits.length(); i++) {
                        JSONObject usr = listproduits.getJSONObject(i);
                        Supplier f = new Supplier();
                        f.setId(usr.getString("idfournisseur"));
                        f.setNom(usr.getString("nom"));
                        f.setTel(usr.getString("tel"));
                        f.setFax(usr.getString("fax"));
                        f.setFix(usr.getString("fix"));
                        f.setAdresse(usr.getString("adresse"));
                        f.setEmail(usr.getString("email"));
                        //  m.setImage(URLs.IpBackend + "clients/client.png");
                        ListF.add(f);
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Fournisseur Found  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            FourniseurAdapter fourniseurAdapter =
                    new FourniseurAdapter(FournisseurListActivity.this, ListF);
            list.setAdapter(fourniseurAdapter);
        }
    }
}
