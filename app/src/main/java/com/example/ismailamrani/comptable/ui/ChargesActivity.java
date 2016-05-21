package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;

import butterknife.ButterKnife;

public class ChargesActivity extends ColoredStatusBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);
        ButterKnife.bind(this);

        setupActionBar();
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Charges");
    }
}
