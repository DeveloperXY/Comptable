package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.SupplierAdapter;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.ui.base.RefreshableActivity;
import com.example.ismailamrani.comptable.utils.GridSpacingItemDecoration;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 29/05/2016.
 */
public class SuppliersActivity extends RefreshableActivity {

    List<Supplier> mSuppliers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fournisseur);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();

        fetchSuppliers();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Fournisseurs");
    }

    @Override
    public void onAddPressed() {
        finish();
        startActivity(new Intent(this, AddFournisseurActivity.class));
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.SUPPLIERS;
    }

    private void fetchSuppliers() {
        JSONObject params = JSONUtils.bundleCompanyIDToJSON(
                mDatabaseAdapter.getUserCompanyID());
        sendHTTPRequest(PhpAPI.getFournisseur, params, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        try {
                            if (status == 1) {
                                JSONArray suppliers = response.getJSONArray("fournisseur");
                                mSuppliers = Supplier.parseSuppliers(suppliers);

                                runOnUiThread(() -> {
                                    populateRecyclerView();
                                    progressBar.setVisibility(View.INVISIBLE);
                                });

                            } else
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                                        "No suppliers found.", Toast.LENGTH_LONG).show());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                                "Network error.", Toast.LENGTH_LONG).show());
                    }
                });
    }

    private void populateRecyclerView() {
        SupplierAdapter supplierAdapter = new SupplierAdapter(this, mSuppliers);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        dataRecyclerView.setLayoutManager(layoutManager);
        dataRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        dataRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dataRecyclerView.setAdapter(supplierAdapter);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
