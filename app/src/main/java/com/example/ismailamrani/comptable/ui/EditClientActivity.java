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
public class EditClientActivity extends ColoredStatusBarActivity {

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

    private Client selectedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_add);
        ButterKnife.bind(this);

        populateFieldsWithSelectedClientData();

        ajouter.setOnClickListener(v -> {
            selectedClient.setNomPrenom(nomprenom.getText().toString());
            selectedClient.setTel(tel.getText().toString());
            selectedClient.setAdresse(adresse.getText().toString());
            selectedClient.setEmail(email.getText().toString());
            selectedClient.setUrl(PhpAPI.editClient);

            updateClient();
        });
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

    private void updateClient() {
        Map<String, String> data = new HashMap<>();
        data.put("ID", selectedClient.getId());
        data.put("NomPrenom", selectedClient.getNomPrenom());
        data.put("Tel", selectedClient.getTel());
        data.put("Adresse", selectedClient.getAdresse());
        data.put("Email", selectedClient.getEmail());

        sendHTTPRequest(selectedClient.getUrl(), data, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.CLIENT_UPDATED);
                        finish();
                        runOnUiThread(() -> Toast.makeText(EditClientActivity.this,
                                "Client altered.", Toast.LENGTH_SHORT).show());
                    }
                });
    }
}
