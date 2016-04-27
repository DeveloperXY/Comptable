package com.example.ismailamrani.comptable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class Vente extends Activity {
    private static final String TAG = Vente.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vente);
        Log.d(TAG, TAG);

    }

}
