package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.LocaleAdapter;
import com.example.ismailamrani.comptable.models.Local;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountingHomeActivity extends AnimatedActivity {

    @Bind(R.id.localeListView)
    ListView localeListView;
    @Bind(R.id.totalProfitLabel)
    TextView totalProfitLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptabilite_home);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
        setupLocalesListView();
        fetchTotalProfit();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Total Profit");
        mActionBar.disableAddButton();
    }

    private void setupLocalesListView() {
        List<Local> locales = mDatabaseAdapter.retrieveCurrentLocales();
        LocaleAdapter localeAdapter = new LocaleAdapter(this, locales);
        localeAdapter.setListener(localeID -> {
            Intent intent = new Intent(this, AccountingDetailsActivity.class);
            intent.putExtra("localeID", localeID);
            startActivity(intent);
        });
        localeListView.setAdapter(localeAdapter);
    }

    private void fetchTotalProfit() {
        JSONObject data = JSONUtils.merge(
                JSONUtils.bundleCompanyIDToJSON(mDatabaseAdapter.getUserCompanyID()),
                JSONUtils.bundleLocaleIDToJSON(mDatabaseAdapter.getCurrentLocaleID()));

        sendHTTPRequest(PhpAPI.getComptabilite, data, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 1) {
                            try {
                                JSONObject details = response.getJSONArray("comptabilite")
                                        .getJSONObject(0);

                                runOnUiThread(() -> {
                                    try {
                                        totalProfitLabel.setText(
                                                details.getString("ProfitTotalSociete") + " DH");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(AccountingHomeActivity.this,
                                "Network error.", Toast.LENGTH_SHORT).show());
                    }
                });
    }
}
