package com.example.ismailamrani.comptable.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Brahim on 24/03/2016.
 */
public class AddLocalActivity extends ColoredStatusBarActivity {
    EditText adresse_local, ville_local, pays_local, tel_local, fix_local, fax_local, email_local, activite_local;
    String adresse, ville, pays, tel, fix, fax, email, activite;
    Button ajouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlocal);
        adresse_local = (EditText) findViewById(R.id.adresse_local);
        ville_local = (EditText) findViewById(R.id.ville_local);
        pays_local = (EditText) findViewById(R.id.pays_local);
        tel_local = (EditText) findViewById(R.id.tel_local);
        fix_local = (EditText) findViewById(R.id.fix_local);
        fax_local = (EditText) findViewById(R.id.fax_local);
        email_local = (EditText) findViewById(R.id.email_local);
        activite_local = (EditText) findViewById(R.id.activite_local);
        ajouter = (Button) findViewById(R.id.ajouterl);
        ajouter.setOnClickListener(v -> {
            adresse = adresse_local.getText().toString();
            ville = ville_local.getText().toString();
            pays = pays_local.getText().toString();
            tel = tel_local.getText().toString();
            fix = fix_local.getText().toString();
            fax = fax_local.getText().toString();
            email = email_local.getText().toString();
            activite = activite_local.getText().toString();

            new addlocal().execute(PhpAPI.addlocal);
        });
    }

    private class addlocal extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) connection;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                Map<String, Object> Params = new LinkedHashMap<>();
                Params.put("Adresse", adresse);
                Params.put("Ville", ville);
                Params.put("Pays", pays);
                Params.put("Tel", tel);
                Params.put("Fix", fix);
                Params.put("Fax", fax);
                Params.put("Email", email);
                Params.put("Activite", activite);
                Params.put("Societe", 1);


                OutputStream os = connection.getOutputStream();
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
                int rep = j.getInt("success");
                if (rep == 1) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bien Ajouter", Toast.LENGTH_LONG);
                    toast.show();
                } else if (rep == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "erreur  !!!!", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class getQuery {
        public String getQuery(Map<String, Object> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            return result.toString();
        }
    }

    public class convertInputStreamToString {
        public String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }
    }
}
