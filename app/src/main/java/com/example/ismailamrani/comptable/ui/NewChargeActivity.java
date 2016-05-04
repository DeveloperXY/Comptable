package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;

/**
 * Created by Redouane on 06/04/2016.
 */
public class NewChargeActivity extends ColoredStatusBarActivity
        implements OGActionBarInterface {

    OGActionBar MyActionBar;
    ImageView spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charges_add);

        setupActionBar();

        MyActionBar.disableAddButton();
        spinner = (ImageView) findViewById(R.id.spinner);
        spinner.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),
                        SpinnerChargeActivity.class)));
    }

    private void setupActionBar() {
        MyActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        MyActionBar.setActionBarListener(this);
        MyActionBar.setTitle("Ajouter Une Charge");
    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }
}
