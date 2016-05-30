package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.WithDrawerActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 21/05/2016.
 */
public class AddChargeActivity extends WithDrawerActivity {

    @Bind(R.id.priceField)
    EditText priceField;
    @Bind(R.id.descriptionField)
    EditText descriptionField;
    @Bind(R.id.saveButton)
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charges);
        ButterKnife.bind(this);

        setupActionBar();
        setupTextWatcher();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Ajouter Une Charge");
        mActionBar.disableAddButton();
    }

    /**
     * Sets a TextWatcher on the price & description text fields.
     */
    private void setupTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        priceField.addTextChangedListener(textWatcher);
        descriptionField.addTextChangedListener(textWatcher);
    }

    /**
     * Sets the 'enabled' state of the 'save' button based on the length
     * of the contents of the activity's fields.
     */
    private void checkFields() {
        saveButton.setEnabled(
                priceField.getText().toString().length() != 0 &&
                        descriptionField.getText().toString().length() != 0);
    }

    public void onSave(View view) {
        String price = priceField.getText().toString();
        String description = descriptionField.getText().toString();
        JSONObject params = JSONUtils.bundleChargeDataToJSON(
                mDatabaseAdapter.getCurrentLocaleID(), price, description);

        sendHTTPRequest(PhpAPI.addCharge, params, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 1) {
                            setResult(ResultCodes.CHARGE_CREATED);
                            finish();
                        } else
                            runOnUiThread(() -> Toast.makeText(AddChargeActivity.this,
                                    "Unknown error.", Toast.LENGTH_SHORT).show());
                    }
                });
    }
}
