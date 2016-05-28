package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;

import butterknife.ButterKnife;

public class ComptabiliteDetailsActivity extends AnimatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptabilite_details);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Comptabilité");
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.COMPTABILITE;
    }
}
