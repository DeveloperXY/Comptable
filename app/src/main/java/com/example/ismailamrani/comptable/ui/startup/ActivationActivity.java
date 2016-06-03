package com.example.ismailamrani.comptable.ui.startup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.DatabaseAdapter;
import com.example.ismailamrani.comptable.models.Activation;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.ui.ActivityTransition;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivationActivity extends ColoredStatusBarActivity {

    private DatabaseAdapter databaseAdapter;

    @Bind(R.id.activationField)
    EditText activationField;
    @Bind(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        ButterKnife.bind(this);

        databaseAdapter = DatabaseAdapter.getInstance(this);
    }

    public void onActivate(View view) {
        String serial = activationField.getText().toString();
        if (!TextUtils.isEmpty(serial)) {
            mLoadingDialog.show();
            sendHTTPRequest(
                    PhpAPI.activateApp,
                    JSONUtils.bundleSerialToJSON(serial),
                    Method.POST,
                    new ActivationListener()
            );
        } else
            Toast.makeText(this, "Activation code required.", Toast.LENGTH_LONG).show();
    }

    /**
     * A listener on the activation procedure.
     */
    public class ActivationListener extends SuccessRequestListener {
        @Override
        public void onRequestSucceeded(JSONObject response) {
            try {
                String message = response.getString("message");

                runOnUiThread(() -> {
                    Toast.makeText(ActivationActivity.this,
                            message, Toast.LENGTH_SHORT).show();
                    try {
                        Activation activation = new Activation(
                                response.getJSONArray("activation").getJSONObject(0));
                        databaseAdapter.activateApplication(activation);

                        activityShouldFinish();
                        ActivityTransition.startActivityWithSharedElement(
                                ActivationActivity.this, LoginActivity.class,
                                imageView, "header");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRequestFailed(int status, JSONObject response) {
            try {
                String message = response.getString("message");
                runOnUiThread(() -> Toast.makeText(
                        ActivationActivity.this, message, Toast.LENGTH_SHORT).show());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
