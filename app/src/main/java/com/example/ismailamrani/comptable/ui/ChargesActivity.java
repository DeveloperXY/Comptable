package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Local;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
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
    @Bind(R.id.localLabel)
    TextView localLabel;
    @Bind(R.id.priceField)
    EditText priceField;
    @Bind(R.id.descriptionField)
    EditText descriptionField;
    @Bind(R.id.saveButton)
    Button saveButton;

    private List<Local> locales;
    private Local selectedLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);
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
                                    locales = Local.parseLocales(array);
                                    showLocalesDialog();

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

    private void showLocalesDialog() {
        List<String> stringLocales = Stream.of(locales)
                .map(Local::getAddress)
                .collect(Collectors.toList());

        new LocalChooserDialog(this)
                .whoseItemsAre(stringLocales)
                .whoseSearchHintIs("Search for locales...")
                .runWhenItemSelected(item -> {
                    selectedLocal = Stream.of(locales)
                            .filter(l -> l.getAddress().equals(item))
                            .findFirst().get();

                    localLabel.setText(selectedLocal.getAddress());
                })
                .show();
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Ajouter Une Charge");
        mActionBar.disableAddButton();
    }
}
