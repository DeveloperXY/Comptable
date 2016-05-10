package com.example.ismailamrani.comptable.ui.startup;

import android.content.Intent;
import android.os.Bundle;

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
        Class<?> target;

        if (activation == null || !activation.isActivated())
            target = null;
        else {
            if (isUserLoggedIn())
                target = HomeActivity.class;
            else
                target = LoginActivity.class;
        }

        if (target != null)
            startActivity(new Intent(this, target));
    }

    /**
     * @return true if a user is already logged in, false otherwise.
     */
    private boolean isUserLoggedIn() {
        return databaseAdapter.getLoggedUser() != null;
    }
}