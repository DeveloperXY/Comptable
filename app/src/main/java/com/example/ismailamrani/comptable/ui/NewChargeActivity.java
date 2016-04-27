package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.CustumItems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.R;

/**
 * Created by Redouane on 06/04/2016.
 */
public class NewChargeActivity extends Activity implements OGActionBarInterface {
    private static final String TAG = NewChargeActivity.class.getSimpleName();

    OGActionBar MyActionBar;
    ImageView spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charges_add);
        Log.d(TAG, TAG);

        MyActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        MyActionBar.setActionBarListener(this);
        MyActionBar.setTitle("Ajouter Une Charge");
        MyActionBar.AddDisable();
        spinner = (ImageView)findViewById(R.id.spinner);
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SpinnerChargeActivity.class));
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
