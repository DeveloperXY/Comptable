package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.FourniseurAdapter;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Redouane on 08/04/2016.
 */
public class SuppliersActivity extends AnimatedActivity {

    @Bind(R.id.Listfournisseur)
    ListView list;
    ArrayList<Supplier> mSuppliers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fournisseur);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();

        fetchSuppliers();
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

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.SUPPLIERS;
    }

    private void fetchSuppliers() {
        JSONObject params = JSONUtils.bundleCompanyIDToJSON(
                mDatabaseAdapter.getUserCompanyID());
        sendHTTPRequest(PhpAPI.getFournisseur, params, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        try {
                            if (status == 1) {
                                JSONArray listproduits = response.getJSONArray("fournisseur");

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
                                    mSuppliers.add(f);
                                }
                            } else
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                                        "No Fournisseur Found  !!!!", Toast.LENGTH_LONG).show());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        FourniseurAdapter fourniseurAdapter =
                                new FourniseurAdapter(SuppliersActivity.this, mSuppliers);
                        runOnUiThread(() -> list.setAdapter(fourniseurAdapter));
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                                "Network error.", Toast.LENGTH_LONG).show());
                    }
                });
    }
}
