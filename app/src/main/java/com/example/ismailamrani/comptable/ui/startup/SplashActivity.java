package com.example.ismailamrani.comptable.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ismailamrani.comptable.models.Activation;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;

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
        Log.i("ACTIVATION", "Activation IS NULL: " + (activation == null));
        Class<?> target;

        if (activation == null || !activation.isActivated()) {
            if (activation != null)
                Log.i("ACTIVATION", activation.toString());
            target = ActivationActivity.class;
        }
        else {
            if (isUserLoggedIn())
                target = HomeActivity.class;
            else
                target = LoginActivity.class;
        }

        startActivity(new Intent(this, target));
        finish();
    }

    /**
     * @return true if a user is already logged in, false otherwise.
     */
    private boolean isUserLoggedIn() {
        return databaseAdapter.getLoggedUser() != null;
    }
}