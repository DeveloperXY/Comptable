package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Client;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
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
public class AddClientActivity extends ColoredStatusBarActivity {

    @Bind(R.id.nomcomletclient)
    EditText nomprenom;
    @Bind(R.id.numtel)
    EditText tel;
    @Bind(R.id.adresse)
    EditText adresse;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.addClient)
    Button ajouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_add);
        ButterKnife.bind(this);

        ajouter.setOnClickListener(v -> {
            Client client = validateClientInfos(
                    nomprenom.getText().toString(),
                    tel.getText().toString(),
                    adresse.getText().toString(),
                    email.getText().toString());

            if (client != null)
                createClient(client);
        });
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
            dialogTitle = "Invalid supplier name.";
            dialogMessage = "You need to specify the name of the client.";
        } else if (!gsmStatus) {
            dialogTitle = "Invalid phone number.";
            dialogMessage = "You need to specify a valid phone number.";
        } else if (!addressStatus) {
            dialogTitle = "Invalid address.";
            dialogMessage = "You need to specify the address of the client.";
        } else {
            dialogTitle = "Invalid email.";
            dialogMessage = "You need to specify the email of the client.";
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

        sendHTTPRequest(client.getUrl(), map, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.CLIENT_CREATED);
                        finish();
                        runOnUiThread(() -> Toast.makeText(AddClientActivity.this,
                                "Client successfully created.", Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onRequestFailed(int status, JSONObject response) {
                        runOnUiThread(() -> Toast.makeText(AddClientActivity.this,
                                "Unknown error.", Toast.LENGTH_LONG).show());
                    }
                });
    }
}
