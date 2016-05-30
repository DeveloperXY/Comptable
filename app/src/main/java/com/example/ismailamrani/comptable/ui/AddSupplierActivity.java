package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Redouane on 08/04/2016.
 * Altered by Mohammed Aouf ZOUAG on 30/05/2016.
 */
public class AddSupplierActivity extends ColoredStatusBarActivity {

    @Bind(R.id.nomcomletclient)
    EditText nom;
    @Bind(R.id.numerofixFour)
    EditText tel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fournisseur_add);
        ButterKnife.bind(this);

        Picasso.with(this).load(R.drawable.flogo)
                .transform(new CropCircleTransformation())
                .into(ImageProfil);

        addFournisseur.setOnClickListener(v -> {
            Supplier supplier = new Supplier(
                    "",
                    nom.getText().toString(),
                    gsm.getText().toString(),
                    adresse.getText().toString(),
                    tel.getText().toString(),
                    fax.getText().toString(),
                    email.getText().toString(),
                    "",
                    PhpAPI.addFournisseur);

            createSupplier(supplier);
        });
    }

    private void createSupplier(Supplier supplier) {
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(Pair.create("Nom", supplier.getNom()));
        params.add(Pair.create("Tel", supplier.getTel()));
        params.add(Pair.create("Adresse", supplier.getAdresse()));
        params.add(Pair.create("Fix", supplier.getFix()));
        params.add(Pair.create("Fax", supplier.getFax()));
        params.add(Pair.create("Email", supplier.getEmail()));

        sendHTTPRequest(supplier.getUrl(), params, Method.POST,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 1) {
                            finish();
                            runOnUiThread(() -> {
                                Toast.makeText(AddSupplierActivity.this,
                                        "Supplier successfully created.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AddSupplierActivity.this, SuppliersActivity.class));
                            });

                        } else if (status == 0) {
                            runOnUiThread(() -> Toast.makeText(AddSupplierActivity.this,
                                    "There was an error while processing your request.",
                                    Toast.LENGTH_LONG).show());
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(AddSupplierActivity.this,
                                "Network error.", Toast.LENGTH_LONG).show());
                    }
                });
    }
}
