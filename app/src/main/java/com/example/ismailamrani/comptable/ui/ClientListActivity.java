package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.ismailamrani.comptable.Adapters.ClientAdapter;
import com.example.ismailamrani.comptable.CustumItems.ColorStatutBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.LocalData.URLs;
import com.example.ismailamrani.comptable.Models.ClientModel;
import com.example.ismailamrani.comptable.R;
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
 * Created by Redouane on 31/03/2016.
 */
public class ClientListActivity extends Activity implements OGActionBarInterface{
    private static final String TAG = ClientListActivity.class.getSimpleName();
    OGActionBar myactionbar;
    ListView list;
    Context context;
    ArrayList<ClientModel> List = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG);

        new ColorStatutBar().ColorStatutBar(this);
        context = this;

        setContentView(R.layout.activity_client);
        myactionbar = (OGActionBar)findViewById(R.id.MyActionBar);
        myactionbar.setActionBarListener(this);
        myactionbar.setTitle("Client");

        list = (ListView)findViewById(R.id.Listclient);
        new GetData().execute(URLs.getClient);

    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddClientActivity.class));

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
                JSONArray listproduits = j.getJSONArray("client");

                for (int i = 0; i < listproduits.length(); i++) {
                    JSONObject usr = listproduits.getJSONObject(i);
                    ClientModel m = new ClientModel();
                    m.setId(usr.getString("idclient"));
                    m.setNomPrenom(usr.getString("nom"));
                    m.setTel(usr.getString("tel"));
                    m.setAdresse(usr.getString("adresse"));
                    m.setImage(URLs.IpBackend + "clients/client.png");
                    List.add(m);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ClientAdapter adapter = new ClientAdapter(context,List);
            list.setAdapter(adapter);


        }
    }

}
