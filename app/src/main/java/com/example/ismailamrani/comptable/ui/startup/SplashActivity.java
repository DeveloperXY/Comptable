package com.example.ismailamrani.comptable.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ismailamrani.comptable.adapters.DatabaseAdapter;
import com.example.ismailamrani.comptable.models.Activation;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.ui.DialogUtil;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohammed Aouf ZOUAG on 04/05/2016.
 */
public class SplashActivity extends ColoredStatusBarActivity {

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseAdapter = DatabaseAdapter.getInstance(this);
        Activation activation = databaseAdapter.getCurrentActivation();

        if (activation == null || !activation.isActivated()) {
            finish();
            startActivity(new Intent(this, ActivationActivity.class));
        }
        else
            checkRemoteActivation(activation.getCode());
    }

    /**
     * Checks if this serial is activated on the distant server.
     *
     * @param serial to be checked.
     */
    private void checkRemoteActivation(String serial) {

        sendHTTPRequest(PhpAPI.getActivationStatus,
                JSONUtils.bundleSerialToJSON(serial),
                Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        runOnUiThread(() -> {
                            Class<?> target;

                            if (status == 1) {
                                try {
                                    int activationStatus = response.getInt("activationStatus");
                                    if (activationStatus == 1) {
                                        // The application is activated, check for session
                                        if (isUserLoggedIn())
                                            target = HomeActivity.class;
                                        else
                                            target = LoginActivity.class;
                                    } else {
                                        target = ActivationActivity.class;
                                        Toast.makeText(SplashActivity.this,
                                                "Your activation code is not active. Please contact the administration.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    finish();
                                    startActivity(new Intent(SplashActivity.this, target));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (status == 0) {
                                DialogUtil.showDialog(SplashActivity.this, "Apologies",
                                        "It seems like we are missing your activation code. Please contact us as soon as possible.",
                                        "Dismiss", null,
                                        dialog -> {
                                            finish();
                                            startActivity(new Intent(SplashActivity.this, ActivationActivity.class));
                                        });
                            }
                        });
                    }
                }
        );
    }

    /**
     * @return true if a user is already logged in, false otherwise.
     */
    private boolean isUserLoggedIn() {
        return databaseAdapter.getLoggedUser() != null;
    }
}