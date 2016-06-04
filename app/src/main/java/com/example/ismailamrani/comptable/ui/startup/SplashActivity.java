package com.example.ismailamrani.comptable.ui.startup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activation activation = mDatabaseAdapter.getCurrentActivation();

        if (activation == null || !activation.isActivated()) {
            finish();
            startActivity(new Intent(this, ActivationActivity.class));
        } else
            checkRemoteActivation(activation.getCode());
    }

    /**
     * Checks if this serial is activated on the distant server.
     *
     * @param serial to be checked.
     */
    private void checkRemoteActivation(String serial) {
        mLoadingDialog.show();

        sendHTTPRequest(PhpAPI.getActivationStatus,
                JSONUtils.bundleSerialToJSON(serial),
                Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(final JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Class<?> target;

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
                                        mDatabaseAdapter.removeCurrentActivation();
                                    }

                                    finish();
                                    startActivity(new Intent(SplashActivity.this, target));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onRequestFailed(int status, JSONObject response) {
                        mDatabaseAdapter.removeCurrentActivation();
                        DialogUtil.showDialog(SplashActivity.this, "Apologies",
                                "It seems like we are missing your activation code. Please contact us as soon as possible.",
                                "Dismiss", null,
                                new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        finish();
                                        startActivity(new Intent(SplashActivity.this,
                                                ActivationActivity.class));
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
        return mDatabaseAdapter.getLoggedUser() != null;
    }
}