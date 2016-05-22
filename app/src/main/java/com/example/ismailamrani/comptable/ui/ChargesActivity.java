package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ChargeAdapter;
import com.example.ismailamrani.comptable.models.Charge;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.ListComparison;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.utils.SpacesItemDecoration;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargesActivity extends ColoredStatusBarActivity {

    @Bind(R.id.chargesRecyclerView)
    RecyclerView chargesRecyclerView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.progressBar)
    ProgressBar chargeProgressbar;

    @Bind(R.id.totalPriceLabel)
    TextView totalPriceLabel;

    private ChargeAdapter mChargeAdapter;
    private List<Charge> mCharges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);
        ButterKnife.bind(this);

        setupActionBar();
        setupRecyclerView();
        setupSwipeRefresh();

        fetchChargeItems();
    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddChargeActivity.class));
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Charges");
    }

    private void setupRecyclerView() {
        mCharges = new ArrayList<>();
        chargesRecyclerView.setHasFixedSize(true);
        chargesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chargesRecyclerView.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipeRefresh1,
                R.color.swipeRefresh2,
                R.color.swipeRefresh3,
                R.color.swipeRefresh4
        );
    }

    private void refresh() {
        if (!swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(true);

        fetchChargeItems();
    }

    private void stopSwipeRefresh() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    private void fetchChargeItems() {
        JSONObject data = JSONUtils.bundleLocaleIDToJSON(
                DatabaseAdapter.getInstance(this).getCurrentLocaleID());
        sendHTTPRequest(PhpAPI.getChargeByLocaleID, data, Method.POST,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("charge");
                            List<Charge> charges = Charge.parseCharges(jsonArray);

                            // To avoid the refresh flicker caused by the call to
                            // populateRecyclerView(), check if the newly parsed
                            // list of Charge objects is exactly the same as the
                            // old one
                            if (ListComparison.areEqual(mCharges, charges))
                                runOnUiThread(() -> stopSwipeRefresh());
                            else {
                                mCharges = charges;
                                runOnUiThread(() -> {
                                    chargesRecyclerView.scrollToPosition(0);
                                    populateRecyclerView();
                                    calculateTotalPrice();
                                    stopSwipeRefresh();
                                    chargeProgressbar.setVisibility(View.INVISIBLE);
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> {
                            Toast.makeText(ChargesActivity.this, "Error while retrieving charges.",
                                    Toast.LENGTH_LONG).show();
                            chargeProgressbar.setVisibility(View.INVISIBLE);
                            stopSwipeRefresh();
                        });
                    }
                });
    }

    private void calculateTotalPrice() {
        totalPriceLabel.setText(mChargeAdapter.getTotalPrice() + " DH");
    }

    private void populateRecyclerView() {
        if (mChargeAdapter == null) {
            mChargeAdapter = new ChargeAdapter(this, mCharges);
            chargesRecyclerView.setAdapter(mChargeAdapter);
        } else
            mChargeAdapter.animateTo(mCharges);
    }
}
