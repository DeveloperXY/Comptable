package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ChargeAdapter;
import com.example.ismailamrani.comptable.models.Charge;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.JSONUtils;
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

    private ChargeAdapter mChargeAdapter;
    private List<Charge> mCharges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);
        ButterKnife.bind(this);

        setupActionBar();
        setupRecyclerView();
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

        refresh();
    }

    private void refresh() {
        fetchChargeItems();
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
                            mCharges = Charge.parseCharges(jsonArray);
                            runOnUiThread(() -> populateRecyclerView());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        Toast.makeText(ChargesActivity.this, "Error while retrieving charges.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void populateRecyclerView() {
        if (mChargeAdapter == null) {
            mChargeAdapter = new ChargeAdapter(this, mCharges);
            chargesRecyclerView.setAdapter(mChargeAdapter);
        } else
            mChargeAdapter.animateTo(mCharges);
    }
}
