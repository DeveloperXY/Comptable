package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ChargeAdapter;
import com.example.ismailamrani.comptable.models.Charge;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.ListComparison;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.utils.SpacesItemDecoration;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargesActivity extends AnimatedActivity {

    private static final int REQUEST_ADD_CHARGE = 3;

    @Bind(R.id.chargesRecyclerView)
    RecyclerView chargesRecyclerView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    /**
     * The view to be displayed in case a network error occur.
     */
    @Bind(R.id.errorChargeLayout)
    RelativeLayout errorLayout;

    /**
     * The view to be displayed in case there were no items to show.
     */
    @Bind(R.id.emptyChargeLayout)
    RelativeLayout emptyView;

    @Bind(R.id.footerLayout)
    RelativeLayout footerLayout;

    @Bind(R.id.emptyMessageLabel)
    TextView emptyMessageLabel;

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

        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Charges");
    }

    private void setupRecyclerView() {
        mCharges = new ArrayList<>();
        chargesRecyclerView.setHasFixedSize(true);
        chargesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chargesRecyclerView.addItemDecoration(new SpacesItemDecoration(4));
        emptyMessageLabel.setText("There is no data to show.\nClick to refresh.");
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

                            runOnUiThread(() -> {
                                // To avoid the refresh flicker caused by the call to
                                // populateRecyclerView(), check if the newly parsed
                                // list of Charge objects is exactly the same as the
                                // old one
                                if (!ListComparison.areEqual(mCharges, charges)) {
                                    mCharges = charges;
                                    chargesRecyclerView.scrollToPosition(0);
                                    populateRecyclerView();
                                    calculateTotalPrice();
                                }

                                if(mCharges == null)
                                    mCharges = new ArrayList<>();

                                stopSwipeRefresh();
                                toggleRecyclerviewState();
                                chargeProgressbar.setVisibility(View.INVISIBLE);
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> {
                            Toast.makeText(ChargesActivity.this, "Error while retrieving charges.",
                                    Toast.LENGTH_LONG).show();

                            if (errorLayout.getVisibility() != View.VISIBLE) {
                                errorLayout.setVisibility(View.VISIBLE);
                                chargesRecyclerView.setVisibility(View.INVISIBLE);
                                chargeProgressbar.setVisibility(View.INVISIBLE);
                                footerLayout.setVisibility(View.INVISIBLE);
                            }

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

    private void toggleRecyclerviewState() {
        int emptyViewVis = mCharges.size() == 0 ? View.VISIBLE : View.INVISIBLE;
        emptyView.setVisibility(emptyViewVis);
        chargesRecyclerView.setVisibility(mCharges.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
        // The footer layout should only be displayed in case the empty view wasn't
        footerLayout.setVisibility(emptyViewVis == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
    }
}
