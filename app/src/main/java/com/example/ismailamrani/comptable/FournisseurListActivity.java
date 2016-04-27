package com.example.ismailamrani.comptable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.Adapters.FourniseurAdapter;
import com.example.ismailamrani.comptable.CustumItems.ColorStatutBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.LocalData.URLs;
import com.example.ismailamrani.comptable.Models.Fournisseur;
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
 * Created by Redouane on 08/04/2016.
 */
public class FournisseurListActivity extends Activity implements OGActionBarInterface {
    private static final String TAG = FournisseurListActivity.class.getSimpleName();

    OGActionBar myactionbar;
    Context context;
    ListView list;
    ArrayList<Fournisseur> ListF = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG);
        new ColorStatutBar().ColorStatutBar(this);
        context=this;

        setContentView(R.layout.activity_fournisseur);
        myactionbar = (OGActionBar)findViewById(R.id.MyActionBar);
        myactionbar.setActionBarListener(this);
        myactionbar.setTitle("Fournisseur");
        list =(ListView)findViewById(R.id.Listfournisseur);
        new GetData().execute(URLs.getFournisseur);
    }

    @Override
    public void onMenuPressed() {

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
                        Fournisseur f=new Fournisseur();
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
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "No Fournisseur Found  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            FourniseurAdapter fourniseurAdapter = new FourniseurAdapter(context,ListF);
            list.setAdapter(fourniseurAdapter);


        }
    }

}
