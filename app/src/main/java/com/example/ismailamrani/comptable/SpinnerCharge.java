package com.example.ismailamrani.comptable;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListView;

import com.example.ismailamrani.comptable.Adapters.SpinnerAdapter;
import com.example.ismailamrani.comptable.LocalData.ToDelete.GetSpinnerLocal;

/**
 * Created by Redouane on 07/04/2016.
 */
public class SpinnerCharge extends Activity {

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

        list = (ListView)findViewById(R.id.Listspinner);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getApplicationContext(),new GetSpinnerLocal().Getlocal());
        list.setAdapter(spinnerAdapter);
    }
}
