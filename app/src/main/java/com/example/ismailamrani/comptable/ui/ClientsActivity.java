package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ClientAdapter;
import com.example.ismailamrani.comptable.models.ClientModel;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Redouane on 31/03/2016.
 */
public class ClientsActivity extends AnimatedActivity {

    @Bind(R.id.Listclient)
    ListView list;

    ArrayList<ClientModel> List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();

        fetchClients();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Client");
    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddClientActivity.class));
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.CLIENTS;
    }

    private void fetchClients() {
        JSONObject data = JSONUtils.bundleCompanyIDToJSON(
                mDatabaseAdapter.getUserCompanyID());
        sendHTTPRequest(PhpAPI.getClient, data, Method.POST,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        try {
                            if (status == 1) {
                                JSONArray listproduits = response.getJSONArray("client");

                                for (int i = 0; i < listproduits.length(); i++) {
                                    JSONObject usr = listproduits.getJSONObject(i);
                                    ClientModel m = new ClientModel();
                                    m.setId(usr.getString("idclient"));
                                    m.setNomPrenom(usr.getString("nom"));
                                    m.setTel(usr.getString("tel"));
                                    m.setAdresse(usr.getString("adresse"));
                                    m.setImage(PhpAPI.IpBackend + "clients/client.png");
                                    List.add(m);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ClientAdapter adapter = new ClientAdapter(ClientsActivity.this, List);
                        runOnUiThread(() -> list.setAdapter(adapter));
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                                "Network error.", Toast.LENGTH_LONG).show());
                    }
                });
    }
}
