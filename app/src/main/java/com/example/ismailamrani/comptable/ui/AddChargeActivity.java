package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    /**
     * The maximum allowed number of characters for a given description.
     */
    private static final int MAX_DESC_CHAR_COUNT = 50;

    @Bind(R.id.priceField)
    EditText priceField;
    @Bind(R.id.descriptionField)
    EditText descriptionField;
    @Bind(R.id.charCounterLabel)
    TextView charCounterLabel;
    @Bind(R.id.saveButton)
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charges);
        ButterKnife.bind(this);

        setupActionBar();
        setupTextWatchers();
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
    private void setupTextWatchers() {
        priceField.addTextChangedListener(new PriceWatcher());
        descriptionField.addTextChangedListener(new DescriptionWatcher());
    }

    /**
     * Sets the 'enabled' state of the 'save' button based on the length
     * of the contents of the activity's fields.
     */
    private void checkFields() {
        int descriptionLength = descriptionField.getText().toString().length();
        saveButton.setEnabled(
                priceField.getText().toString().length() != 0 &&
                        descriptionLength > 0 && descriptionLength <= MAX_DESC_CHAR_COUNT);
    }

    public void onSave(View view) {
        String price = priceField.getText().toString();
        String description = descriptionField.getText().toString();
        JSONObject params = JSONUtils.bundleChargeDataToJSON(
                mDatabaseAdapter.getCurrentLocaleID(), price, description);

        sendHTTPRequest(PhpAPI.addCharge, params, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.CHARGE_CREATED);
                        finish();
                    }
                });
    }

    /**
     * A TextWatcher on the price text field.
     */
    class PriceWatcher implements TextWatcher {
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
    }

    /**
     * A TextWatcher on the description text field.
     */
    class DescriptionWatcher extends PriceWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);

            String text = s.toString();
            String message;
            int textLength = text.length();

            if (textLength == 49)
                message = "1 character left";
            else if (textLength < 51)
                message = String.format("%d characters left", MAX_DESC_CHAR_COUNT - textLength);
            else
                message = "Maximum description length exceeded";

            charCounterLabel.setText(message);
        }
    }
}
