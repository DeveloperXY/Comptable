package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.localdata.FileProduit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brahim on 07/04/2016.
 */
public class AddLocalFileActivity extends Activity {
    private static final String TAG = AddLocalFileActivity.class.getSimpleName();
    EditText adresse_local, ville_local, pays_local, tel_local, fix_local, fax_local, email_local, activite_local;
    String adresse, ville, pays, tel, fix, fax, email, activite;
    Button ajouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlocal);
        Log.d(TAG, TAG);
        adresse_local = (EditText) findViewById(R.id.adresse_local);
        ville_local = (EditText) findViewById(R.id.ville_local);
        pays_local = (EditText) findViewById(R.id.pays_local);
        tel_local = (EditText) findViewById(R.id.tel_local);
        fix_local = (EditText) findViewById(R.id.fix_local);
        fax_local = (EditText) findViewById(R.id.fax_local);
        email_local = (EditText) findViewById(R.id.email_local);
        activite_local = (EditText) findViewById(R.id.activite_local);
        ajouter = (Button) findViewById(R.id.ajouterl);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adresse = adresse_local.getText().toString();
                ville = ville_local.getText().toString();
                pays = pays_local.getText().toString();
                tel = tel_local.getText().toString();
                fix = fix_local.getText().toString();
                fax = fax_local.getText().toString();
                email = email_local.getText().toString();
                activite = activite_local.getText().toString();
                FileProduit f = new FileProduit("Local.txt");
                String read;
                read = f.read();
                if (read == null) {

                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray listlocal = new JSONArray();
                        JSONObject j = new JSONObject();
                        j.put("Idloc", 1);
                        j.put("Adresse", adresse);
                        j.put("Ville", ville);
                        j.put("Pays", pays);
                        j.put("Tel", tel);
                        j.put("Fix", fix);
                        j.put("Fax", fax);
                        j.put("Email", email);
                        j.put("Activite", activite);
                        listlocal.put(j);
                        jsonObject.put("Local", listlocal);
                        f.write(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(read);
                        JSONArray listlocal = jsonObject.getJSONArray("Local");
                        JSONObject j = new JSONObject();
                        // j.put("Logo",photo);
                        j.put("Idloc", listlocal.length() + 1);
                        j.put("Adresse", adresse);
                        j.put("Ville", ville);
                        j.put("Pays", pays);
                        j.put("Tel", tel);
                        j.put("Fix", fix);
                        j.put("Fax", fax);
                        j.put("Email", email);
                        j.put("Activite", activite);
                        listlocal.put(j);
                        f.write(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

    }

}
