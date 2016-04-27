package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.ismailamrani.comptable.R;


public class VenteActivity extends Activity {
    private static final String TAG = VenteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vente);
        Log.d(TAG, TAG);

    }

}
