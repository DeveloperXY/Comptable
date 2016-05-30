package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ChargeAdapter;
import com.example.ismailamrani.comptable.adapters.DatabaseAdapter;
import com.example.ismailamrani.comptable.models.Charge;
import com.example.ismailamrani.comptable.ui.base.RefreshableActivity;
import com.example.ismailamrani.comptable.utils.decorations.SpacesItemDecoration;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.parsing.ListComparison;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargesActivity extends RefreshableActivity {

    private static final int REQUEST_ADD_CHARGE = 3;

    @Bind(R.id.footerLayout)
    RelativeLayout footerLayout;

    @Bind(R.id.totalPriceLabel)
    TextView totalPriceLabel;

    private ChargeAdapter mChargeAdapter;
    private List<Charge> mCharges;
    private long currentServerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
        setupRecyclerView();
        setupSwipeRefresh();

        fetchChargeItems();
    }

    @Override
    public void onAddPressed() {
        startActivityForResult(new Intent(this, AddChargeActivity.class), REQUEST_ADD_CHARGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_CHARGE:
                switch (resultCode) {
                    case ResultCodes.CHARGE_CREATED:
                        refresh();
                        break;
                }
                break;
        }
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Charges");
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.CHARGES;
    }

    @Override
    protected void setupRecyclerView() {
        super.setupRecyclerView();

        mCharges = new ArrayList<>();
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataRecyclerView.addItemDecoration(new SpacesItemDecoration(4));
        emptyMessageLabel.setText("There is no data to show.\nClick to refresh.");
    }

    @Override
    protected void refresh() {
        fetchChargeItems();
    }

    private void fetchChargeItems() {
        JSONObject data = JSONUtils.bundleLocaleIDToJSON(
                DatabaseAdapter.getInstance(this).getCurrentLocaleID());
        sendHTTPRequest(PhpAPI.getChargeByLocaleID, data, Method.POST,
                new FetchChargesListener());
    }

    private void calculateTotalPrice() {
        totalPriceLabel.setText(mChargeAdapter.getTotalPrice() + " DH");
    }

    private void populateRecyclerView() {
        if (mChargeAdapter == null) {
            mChargeAdapter = new ChargeAdapter(this, mCharges);
            dataRecyclerView.setAdapter(mChargeAdapter);
        } else
            mChargeAdapter.animateTo(mCharges);
    }

    private void toggleRecyclerviewState() {
        int emptyViewVis = mCharges.size() == 0 ? View.VISIBLE : View.INVISIBLE;
        emptyLayout.setVisibility(emptyViewVis);
        dataRecyclerView.setVisibility(mCharges.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
        // The footer layout should only be displayed in case the empty view wasn't
        footerLayout.setVisibility(emptyViewVis == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void handleRequestError() {
        if (errorLayout.getVisibility() != View.VISIBLE)
            footerLayout.setVisibility(View.INVISIBLE);

        super.handleRequestError();
    }

    private class FetchChargesListener implements RequestListener {

        private List<Charge> charges;

        @Override
        public void onRequestSucceeded(JSONObject response) {
            try {
                JSONArray jsonArray = response.getJSONArray("charge");
                charges = Charge.parseCharges(jsonArray);

                // After retrieving the individual list items, retrieve the cuurent
                // timing of the distant server
                sendHTTPRequest(PhpAPI.getServerTime, Method.GET, new ServerTimeListener());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNetworkError() {
            runOnUiThread(ChargesActivity.this::handleRequestError);
        }

        private class ServerTimeListener implements RequestListener {
            @Override
            public void onRequestSucceeded(JSONObject response) {
                try {
                    String serverNowTime = response.getString("date");
                    Log.i("DATE", "CURRENT SERVER DATE: " + serverNowTime);
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    currentServerTime = format.parse(serverNowTime).getTime();

                    setChargesTiming();

                    runOnUiThread(() -> {
                        // To avoid the refresh flicker caused by the call to
                        // populateRecyclerView(), check if the newly parsed
                        // list of Charge objects is exactly the same as the
                        // old one
                        if (!ListComparison.areEqual(mCharges, charges)) {
                            mCharges = charges;
                            dataRecyclerView.scrollToPosition(0);
                            populateRecyclerView();
                            calculateTotalPrice();
                        }

                        if (mCharges == null)
                            mCharges = new ArrayList<>();

                        stopSwipeRefresh();
                        toggleRecyclerviewState();
                        progressBar.setVisibility(View.INVISIBLE);
                    });

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

            private void setChargesTiming() {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Stream.of(charges)
                        .forEach(charge -> {
                            // Calculate the relative time span for each element
                            try {
                                long dateInMillis = format.parse(charge.getDate()).getTime();
                                long secondsDifference = (currentServerTime - dateInMillis) / 1000;

                                if (secondsDifference < 60) {
                                    charge.setDateFrom("A few seconds ago");
                                } else {
                                    charge.setDateFrom(
                                            DateUtils.getRelativeTimeSpanString(
                                                    dateInMillis, currentServerTime,
                                                    DateUtils.MINUTE_IN_MILLIS).toString());
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        });
            }

            @Override
            public void onNetworkError() {
                runOnUiThread(ChargesActivity.this::handleRequestError);
            }
        }
    }
}
