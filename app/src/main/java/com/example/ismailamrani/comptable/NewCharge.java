package com.example.ismailamrani.comptable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBarInterface;

/**
 * Created by Redouane on 06/04/2016.
 */
public class NewCharge extends Activity implements OGActionBarInterface {

    OGActionBar MyActionBar;
    ImageView spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charges_add);

        MyActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        MyActionBar.setActionBarListener(this);
        MyActionBar.setTitle("Ajouter Une Charge");
        MyActionBar.AddDisable();
        spinner = (ImageView)findViewById(R.id.spinner);
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SpinnerCharge.class));
            }
        });

    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }
}
