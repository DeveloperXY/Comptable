package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Client;
import com.example.ismailamrani.comptable.ui.base.AnimatedWithBackArrowActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.ui.DialogUtil;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Redouane on 23/03/2016.
 * Altered by Mohammed Aouf ZOUAG on 01/06/2016.
 */
public class AlterClientActivity extends AnimatedWithBackArrowActivity {

    @Bind(R.id.nomcomletclient)
    EditText nomprenom;
    @Bind(R.id.numtel)
    EditText tel;
    @Bind(R.id.adresse)
    EditText adresse;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.addClient)
    Button saveButton;

    /**
     * This boolean flag indicates whether we're creating a new client, or updating an old one.
     */
    private boolean isUpdating;
    private Client selectedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_add);
        ButterKnife.bind(this);

        setupRevealTransition();

        isUpdating = getIntent().getBooleanExtra("isUpdating", false);
        if (isUpdating)
            populateFieldsWithSelectedClientData();

        setupClickListeners();
    }

    private void setupClickListeners() {
        View.OnClickListener createListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = validateClientInfos(
                        nomprenom.getText().toString(),
                        tel.getText().toString(),
                        adresse.getText().toString(),
                        email.getText().toString());

                if (client != null)
                    createClient(client);
            }
        };

        View.OnClickListener updateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = validateClientInfos(
                        nomprenom.getText().toString(),
                        tel.getText().toString(),
                        adresse.getText().toString(),
                        email.getText().toString());

                if (client != null) {
                    selectedClient.setNomPrenom(client.getNomPrenom());
                    selectedClient.setTel(client.getTel());
                    selectedClient.setAdresse(client.getAdresse());
                    selectedClient.setEmail(client.getEmail());
                    selectedClient.setUrl(PhpAPI.editClient);

                    updateClient();
                }
            }
        };

        saveButton.setOnClickListener(isUpdating ? updateListener : createListener);
    }

    private void populateFieldsWithSelectedClientData() {
        selectedClient = getIntent().getParcelableExtra("client");
        if (selectedClient != null) {
            nomprenom.setText(selectedClient.getNomPrenom());
            tel.setText(selectedClient.getTel());
            adresse.setText(selectedClient.getAdresse());
            email.setText(selectedClient.getEmail());
        }
    }

    public Client validateClientInfos(String name, String gsm, String address, String email) {
        String dialogTitle;
        String dialogMessage;

        boolean nameStatus = name.length() != 0;
        boolean gsmStatus = gsm.length() != 0;
        boolean addressStatus = address.length() != 0;
        boolean emailStatus = email.length() != 0;

        if (nameStatus && gsmStatus && addressStatus && emailStatus)
            return new Client("", name, gsm, address, PhpAPI.addClient, "", email);

        if (!nameStatus) {
            dialogTitle = getString(R.string.invalid_client_name);
            dialogMessage = getString(R.string.client_name_required);
        } else if (!gsmStatus) {
            dialogTitle = getString(R.string.invalid_phone_number);
            dialogMessage = getString(R.string.valid_phone_required);
        } else if (!addressStatus) {
            dialogTitle = getString(R.string.invalid_address);
            dialogMessage = getString(R.string.client_address_required);
        } else {
            dialogTitle = getString(R.string.invalid_email);
            dialogMessage = getString(R.string.client_email_required);
        }

        // Something went wrong: show the error dialog.
        DialogUtil.showDialog(this, dialogTitle, dialogMessage, "OK", null);

        return null;
    }

    private void createClient(Client client) {
        Map<String, String> map = new HashMap<>();
        map.put("companyID", mDatabaseAdapter.getUserCompanyID() + "");
        map.put("NomPrenom", client.getNomPrenom());
        map.put("Tel", client.getTel());
        map.put("Adresse", client.getAdresse());
        map.put("Email", client.getEmail());

        mLoadingDialog.show();
        sendHTTPRequest(client.getUrl(), map, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.CLIENT_CREATED);
                        finish();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AlterClientActivity.this,
                                        R.string.client_created, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onRequestFailed(int status, JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AlterClientActivity.this,
                                        R.string.unknown_error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }

    private void updateClient() {
        Map<String, String> data = new HashMap<>();
        data.put("ID", selectedClient.getId());
        data.put("NomPrenom", selectedClient.getNomPrenom());
        data.put("Tel", selectedClient.getTel());
        data.put("Adresse", selectedClient.getAdresse());
        data.put("Email", selectedClient.getEmail());

        mLoadingDialog.show();
        sendHTTPRequest(selectedClient.getUrl(), data, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.CLIENT_UPDATED);
                        finish();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AlterClientActivity.this,
                                        R.string.client_altered, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}
