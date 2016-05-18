package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Redouane on 06/04/2016.
 */
public class ChargesActivity extends ColoredStatusBarActivity {

    @Bind(R.id.spinner)
    ImageView spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charges_add);
        ButterKnife.bind(this);

        setupActionBar();
        spinner.setOnClickListener(v -> sendHTTPRequest(PhpAPI.getLocal, null, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        runOnUiThread(() -> {
                            if (status == 1) {

                            }
                            else {
                                runOnUiThread(() -> Toast.makeText(ChargesActivity.this,
                                        "There was an error while fetching locales.",
                                        Toast.LENGTH_SHORT).show());
                            }
                        });
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(ChargesActivity.this,
                                "Unable to fetch locales.", Toast.LENGTH_SHORT).show());
                    }
                }));
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Ajouter Une Charge");
        mActionBar.disableAddButton();
    }
}
