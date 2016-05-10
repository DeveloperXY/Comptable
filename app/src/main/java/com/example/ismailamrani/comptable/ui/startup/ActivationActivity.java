package com.example.ismailamrani.comptable.ui.startup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivationActivity extends ColoredStatusBarActivity {

    @Bind(R.id.activationField)
    EditText activationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        ButterKnife.bind(this);
    }

    public void onActivate(View view) {
        String serial = activationField.getText().toString();
        if (!TextUtils.isEmpty(serial)) {
            sendHTTPRequest(
                    PhpAPI.activateApp,
                    serialToJSON(serial),
                    Method.POST,
                    new RequestListener() {
                        @Override
                        public void onRequestSucceeded(JSONObject response, int status) {
                            try {
                                String message = response.getString("message");

                                runOnUiThread(() -> {
                                    if (status == 1) {
                                        Toast.makeText(ActivationActivity.this,
                                                message, Toast.LENGTH_SHORT).show();
                                    }
                                    else if (status == -1) {
                                        Toast.makeText(ActivationActivity.this,
                                                message, Toast.LENGTH_SHORT).show();
                                    }
                                    else if (status == 0) {
                                        Toast.makeText(ActivationActivity.this,
                                                message, Toast.LENGTH_SHORT).show();
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
            );
        }
        else {
            Toast.makeText(this, "Activation code required.", Toast.LENGTH_LONG).show();
        }
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
}
