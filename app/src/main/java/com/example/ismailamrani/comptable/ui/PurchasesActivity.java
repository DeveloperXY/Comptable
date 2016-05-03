package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchasesActivity extends Activity implements OGActionBarInterface {

    private OGActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);
        ButterKnife.bind(this);

        setupActionBar();
    }

    private void setupActionBar() {
        mActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Achats");
        mActionBar.AddDisable();
    }

    @OnClick({R.id.supplierSpinner, R.id.productSpinner})
    public void onSpinnerClick(View view) {
        Class<?> targetActivity = null;
        switch (view.getId()) {
            case R.id.supplierSpinner:

                break;
            case R.id.productSpinner:

                break;
        }
    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }
}
