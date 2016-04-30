package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.os.Bundle;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.ColorStatutBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;

public abstract class CustomActionbarActivity extends Activity
        implements OGActionBarInterface {
    private OGActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ColorStatutBar().ColorStatutBar(this);

        actionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        actionBar.setActionBarListener(this);
        actionBar.setTitle("Stock");
    }
}
