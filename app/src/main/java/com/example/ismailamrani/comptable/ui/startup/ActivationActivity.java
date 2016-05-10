package com.example.ismailamrani.comptable.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Activation;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivationActivity extends ColoredStatusBarActivity {

    private DatabaseAdapter databaseAdapter;

    @Bind(R.id.activationField)
    EditText activationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        ButterKnife.bind(this);

        databaseAdapter = DatabaseAdapter.getInstance(this);
        activationField.addTextChangedListener(new SerialWatcher());
    }

    public void onActivate(View view) {
        String serial = activationField.getText().toString();
        if (!TextUtils.isEmpty(serial)) {
            sendHTTPRequest(
                    PhpAPI.activateApp,
                    serialToJSON(serial),
                    Method.POST,
                    new ActivationListener()
            );
        } else
            Toast.makeText(this, "Activation code required.", Toast.LENGTH_LONG).show();
    }

    private JSONObject serialToJSON(String serial) {
        JSONObject data = new JSONObject();

        try {
            data.put("serial", serial);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * A listener on the activation procedure.
     */
    public class ActivationListener implements RequestListener {
        @Override
        public void onRequestSucceeded(JSONObject response, int status) {
            try {
                String message = response.getString("message");

                runOnUiThread(() -> {
                    Toast.makeText(ActivationActivity.this,
                            message, Toast.LENGTH_SHORT).show();
                    if (status == 1) {
                        try {
                            Activation activation = new Activation(
                                    response.getJSONArray("activation").getJSONObject(0));
                            databaseAdapter.activateApplication(activation);
                            startActivity(new Intent(ActivationActivity.this, LoginActivity.class));
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRequestFailed() {
            runOnUiThread(() -> Toast.makeText(ActivationActivity.this,
                    "Unknown error.", Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * A text watcher on the activation text field.
     */
    public class SerialWatcher implements TextWatcher {

        private boolean isUpdating;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isUpdating) {
                isUpdating = false;
                return;
            }

            String serial = s.toString().replaceAll("([^\\da-zA-Z])", "");
            int position = 0;
            StringBuilder sb = new StringBuilder();

            for (char c : serial.toCharArray()) {
                sb.append(Arrays.asList(4, 8, 12).contains(position) ? "-" + c : c);
                position++;
            }

            isUpdating = true;
            String result = sb.toString().toUpperCase();
            activationField.setText(result.length() > 19 ? result.substring(0, 19) : result);
            activationField.setSelection(activationField.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
