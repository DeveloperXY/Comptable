package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.ui.base.AnimatedWithBackArrowActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.ui.DialogUtil;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Redouane on 08/04/2016.
 * Altered by Mohammed Aouf ZOUAG on 30/05/2016.
 */
public class AlterSupplierActivity extends AnimatedWithBackArrowActivity {

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
    @Bind(R.id.addFournisseur)
    Button saveButton;

    /**
     * This boolean flag indicates whether we're creating a new supplier, or updating an old one.
     */
    private boolean isUpdating;
    private Supplier selectedSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fournisseur_add);
        ButterKnife.bind(this);

        setupRevealTransition();

        isUpdating = getIntent().getBooleanExtra("isUpdating", false);
        if (isUpdating)
            populateFieldsWithSelectedSupplierData();

        setupClickListeners();
    }

    private void setupClickListeners() {
        View.OnClickListener createListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Supplier supplier = validateSupplierInfos(
                        nom.getText().toString(),
                        gsm.getText().toString(),
                        adresse.getText().toString(),
                        fixField.getText().toString(),
                        fax.getText().toString(),
                        email.getText().toString());

                if (supplier != null)
                    createSupplier(supplier);
            }
        };

        View.OnClickListener updateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Supplier supplier = validateSupplierInfos(
                        nom.getText().toString(),
                        gsm.getText().toString(),
                        adresse.getText().toString(),
                        fixField.getText().toString(),
                        fax.getText().toString(),
                        email.getText().toString());

                if (supplier != null) {
                    selectedSupplier.setNom(nom.getText().toString());
                    selectedSupplier.setTel(gsm.getText().toString());
                    selectedSupplier.setFax(fax.getText().toString());
                    selectedSupplier.setFix(fixField.getText().toString());
                    selectedSupplier.setAdresse(adresse.getText().toString());
                    selectedSupplier.setEmail(email.getText().toString());
                    selectedSupplier.setUrl(PhpAPI.editFournisseur);

                    updateSupplier();
                }
            }
        };

        saveButton.setOnClickListener(isUpdating ? updateListener : createListener);
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

    public Supplier validateSupplierInfos(String name, String gsm, String address, String fix,
                                          String fax, String email) {
        String dialogTitle;
        String dialogMessage;

        boolean nameStatus = name.length() != 0;
        boolean gsmStatus = gsm.length() != 0;
        boolean addressStatus = address.length() != 0;
        boolean fixStatus = fix.length() != 0;
        boolean faxStatus = fax.length() != 0;
        boolean emailStatus = email.length() != 0;

        if (nameStatus && gsmStatus && addressStatus && fixStatus && faxStatus && emailStatus)
            return new Supplier("", name, gsm, address, fix, fax, email, "", PhpAPI.addFournisseur);

        if (!nameStatus) {
            dialogTitle = getString(R.string.invalid_supplier_name);
            dialogMessage = getString(R.string.supplier_name_required);
        } else if (!gsmStatus) {
            dialogTitle = getString(R.string.invalid_phone_number);
            dialogMessage = getString(R.string.valid_phone_required);
        } else if (!addressStatus) {
            dialogTitle = getString(R.string.invalid_address);
            dialogMessage = getString(R.string.supplier_address_required);
        } else if (!fixStatus) {
            dialogTitle = getString(R.string.invalid_fix_number);
            dialogMessage = getString(R.string.supplier_fix_required);
        } else if (!faxStatus) {
            dialogTitle = getString(R.string.invalid_fax);
            dialogMessage = getString(R.string.supplier_fax_required);
        } else {
            dialogTitle = getString(R.string.invalid_email);
            dialogMessage = getString(R.string.supplier_email_required);
        }

        // Something went wrong: show the error dialog.
        DialogUtil.showDialog(this, dialogTitle, dialogMessage, getString(R.string.ok), null);

        return null;
    }

    private void createSupplier(Supplier supplier) {
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(Pair.create("Nom", supplier.getNom()));
        params.add(Pair.create("Tel", supplier.getTel()));
        params.add(Pair.create("Adresse", supplier.getAdresse()));
        params.add(Pair.create("Fix", supplier.getFix()));
        params.add(Pair.create("Fax", supplier.getFax()));
        params.add(Pair.create("Email", supplier.getEmail()));
        params.add(Pair.create("companyID", mDatabaseAdapter.getUserCompanyID() + ""));

        mLoadingDialog.show();
        sendHTTPRequest(supplier.getUrl(), params, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.SUPPLIER_CREATED);
                        finish();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AlterSupplierActivity.this,
                                        R.string.supplier_created, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
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

        mLoadingDialog.show();
        sendHTTPRequest(selectedSupplier.getUrl(), data, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.SUPPLIER_UPDATED);
                        finish();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AlterSupplierActivity.this,
                                        R.string.supplier_altered, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}
