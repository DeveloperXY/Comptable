package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.SpinnerAdapter;
import com.example.ismailamrani.comptable.localdata.ToDelete.GetSpinnerLocal;

/**
 * Created by Redouane on 07/04/2016.
 */
public class SpinnerChargeActivity extends ColoredStatusBarActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner_layout);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .7));

        list = (ListView) findViewById(R.id.Listspinner);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getApplicationContext(), new GetSpinnerLocal().Getlocal());
        list.setAdapter(spinnerAdapter);
    }
}
