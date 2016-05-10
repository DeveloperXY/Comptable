package com.example.ismailamrani.comptable.ui.startup;

import android.os.Bundle;

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
    }
}