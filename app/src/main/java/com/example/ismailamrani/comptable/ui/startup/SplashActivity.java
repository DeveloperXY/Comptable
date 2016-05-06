package com.example.ismailamrani.comptable.ui.startup;

import android.content.Intent;
import android.os.Bundle;

import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;

/**
 * Created by Mohammed Aouf ZOUAG on 04/05/2016.
 */
public class SplashActivity extends ColoredStatusBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}