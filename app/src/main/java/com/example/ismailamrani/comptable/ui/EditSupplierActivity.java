package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Redouane on 08/04/2016.
 * Altered by Mohammed Aouf ZOUAG on 31/05/2016.
 */
public class EditSupplierActivity extends ColoredStatusBarActivity {

    @Bind(R.id.nomcomletclient)
    EditText nom;
    @Bind(R.id.numerofixFour)
    EditText fixField;
    @Bind(R.id.numerofaxfour)
    EditText fax;
    @Bind(R.id.numtelfour)
    EditText gsm;
    @Bind(R.id.adressefour)
    EditText adresse;
    @Bind(R.id.emailfour)
    EditText email;
    @Bind(R.id.ImageProfil)
    ImageView ImageProfil;
    @Bind(R.id.addFournisseur)
    LinearLayout addFournisseur;

    private Supplier selectedSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fournisseur_add);
        ButterKnife.bind(this);

        populateFieldsWithSelectedSupplierData();

        Picasso.with(this).load(R.drawable.flogo)
                .transform(new CropCircleTransformation())
                .into(ImageProfil);

        addFournisseur.setOnClickListener(v -> {
            selectedSupplier.setNom(nom.getText().toString());
            selectedSupplier.setTel(gsm.getText().toString());
            selectedSupplier.setFax(fax.getText().toString());
            selectedSupplier.setFix(fixField.getText().toString());
            selectedSupplier.setAdresse(adresse.getText().toString());
            selectedSupplier.setEmail(email.getText().toString());
            selectedSupplier.setUrl(PhpAPI.editFournisseur);

            updateSupplier();
        });
    }

    private void populateFieldsWithSelectedSupplierData() {
        selectedSupplier = getIntent().getParcelableExtra("supplier");
        if (selectedSupplier != null) {
            nom.setText(selectedSupplier.getNom());
            gsm.setText(selectedSupplier.getTel());
            adresse.setText(selectedSupplier.getAdresse());
            fax.setText(selectedSupplier.getFax());
            fixField.setText(selectedSupplier.getFix());
            email.setText(selectedSupplier.getEmail());
        }
    }

    private void updateSupplier() {
        Map<String, String> data = new HashMap<>();
        data.put("ID", selectedSupplier.getId());
        data.put("Nom", selectedSupplier.getNom());
        data.put("Tel", selectedSupplier.getTel());
        data.put("Adresse", selectedSupplier.getAdresse());
        data.put("Fix", selectedSupplier.getFix());
        data.put("Fax", selectedSupplier.getFax());
        data.put("Email", selectedSupplier.getEmail());

        sendHTTPRequest(selectedSupplier.getUrl(), data, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.SUPPLIER_UPDATED);
                        finish();
                        runOnUiThread(() -> Toast.makeText(EditSupplierActivity.this,
                                "Supplier altered.", Toast.LENGTH_SHORT).show());
                    }
                });
    }
}
