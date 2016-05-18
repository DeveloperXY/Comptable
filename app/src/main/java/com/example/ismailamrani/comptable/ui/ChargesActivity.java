package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.models.Local;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.ui.dialogs.ChooserDialog;
import com.example.ismailamrani.comptable.ui.dialogs.LocalChooserDialog;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
                                try {
                                    JSONArray array = response.getJSONArray("local");
                                    List<Local> locales = Local.parseLocales(array);
                                    showLocalesDialog(locales);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
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

    private void showLocalesDialog(List<Local> locales) {
        List<String> stringLocales = Stream.of(locales)
                .map(Local::getAddress)
                .collect(Collectors.toList());

        new LocalChooserDialog(this)
                .whoseItemsAre(stringLocales)
                .whoseSearchHintIs("Search for locales...")
                .runWhenItemSelected(item -> {
                    Toast.makeText(ChargesActivity.this, "Selected: " + item,
                            Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Ajouter Une Charge");
        mActionBar.disableAddButton();
    }
}
