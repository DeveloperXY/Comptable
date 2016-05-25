package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Local;
import com.example.ismailamrani.comptable.ui.base.WithDrawerActivity;
import com.example.ismailamrani.comptable.ui.dialogs.LocalChooserDialog;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Redouane on 06/04/2016.
 */
public class AddChargeActivity extends WithDrawerActivity {

    @Bind(R.id.spinner)
    ImageView spinner;
    @Bind(R.id.localLabel)
    TextView localLabel;
    @Bind(R.id.priceField)
    EditText priceField;
    @Bind(R.id.descriptionField)
    EditText descriptionField;
    @Bind(R.id.saveButton)
    Button saveButton;

    private List<Local> locales;
    /**
     * The currently selected local.
     */
    private Local selectedLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charges);
        ButterKnife.bind(this);

        setupActionBar();
        setupTextWatcher();

        spinner.setOnClickListener(new SpinnerClickListener());
    }

    private void showLocalesDialog() {
        List<String> stringLocales = Stream.of(locales)
                .map(Local::getAddress)
                .collect(Collectors.toList());

        new LocalChooserDialog(this)
                .whoseItemsAre(stringLocales)
                .whoseSearchHintIs("Search for locales...")
                .runWhenItemSelected(item -> {
                    selectedLocal = Stream.of(locales)
                            .filter(l -> l.getAddress().equals(item))
                            .findFirst().get();

                    localLabel.setText(selectedLocal.getAddress());
                    checkFields();
                })
                .show();
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
                localLabel.getText().length() != 0 &&
                        priceField.getText().toString().length() != 0 &&
                        descriptionField.getText().toString().length() != 0);
    }

    public void onSave(View view) {
        String price = priceField.getText().toString();
        String description = descriptionField.getText().toString();
        JSONObject params = JSONUtils.bundleChargeDataToJSON(
                selectedLocal.getId(), price, description);

        sendHTTPRequest(PhpAPI.addCharge, params, Method.POST,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 1) {
                            setResult(ResultCodes.CHARGE_CREATED);
                            finish();
                        }
                        else
                            runOnUiThread(() -> Toast.makeText(AddChargeActivity.this,
                                    "Unknown error.", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(AddChargeActivity.this,
                                "Network error.", Toast.LENGTH_SHORT).show());
                    }
                });
    }

    class SpinnerClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Send the HTTP request only once, at the first time the activity is shown
            if (locales != null)
                showLocalesDialog();
            else
                sendHTTPRequest(PhpAPI.getLocal, null, Method.GET,
                        new RequestListener() {
                            @Override
                            public void onRequestSucceeded(JSONObject response, int status) {
                                runOnUiThread(() -> {
                                    if (status == 1) {
                                        try {
                                            JSONArray array = response.getJSONArray("local");
                                            locales = Local.parseLocales(array);
                                            showLocalesDialog();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        runOnUiThread(() -> Toast.makeText(AddChargeActivity.this,
                                                "There was an error while fetching locales.",
                                                Toast.LENGTH_SHORT).show());
                                    }
                                });
                            }

                            @Override
                            public void onRequestFailed() {
                                runOnUiThread(() -> Toast.makeText(AddChargeActivity.this,
                                        "Unable to fetch locales.", Toast.LENGTH_SHORT).show());
                            }
                        });
        }
    }
}
