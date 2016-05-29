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
import com.example.ismailamrani.comptable.ui.dialogs.SupplierDialog;
import com.example.ismailamrani.comptable.utils.DialogUtil;
import com.example.ismailamrani.comptable.utils.GridSpacingItemDecoration;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.ListComparison;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 29/05/2016.
 */
public class SuppliersActivity extends RefreshableActivity {

    private List<Supplier> mSuppliers;
    private SupplierAdapter supplierAdapter;
    private SupplierDialog mSupplierDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
        setupRecyclerView();
        setupSwipeRefresh();

        refresh();
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
                                List<Supplier> supplierList = Supplier.parseSuppliers(suppliers);

                                if (ListComparison.areEqual(mSuppliers, supplierList))
                                    runOnUiThread(this::handleDataChange);
                                else {
                                    mSuppliers = supplierList;
                                    runOnUiThread(() -> {
                                        handleDataChange();
                                        populateRecyclerView();
                                    });
                                }

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
                        runOnUiThread(() -> handleRequestError());
                    }

                    private void handleDataChange() {
                        toggleRecyclerviewState();
                        progressBar.setVisibility(View.INVISIBLE);
                        stopSwipeRefresh();
                    }
                });
    }

    @Override
    protected void setupRecyclerView() {
        super.setupRecyclerView();
        mSuppliers = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        dataRecyclerView.setLayoutManager(layoutManager);
        dataRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        dataRecyclerView.setItemAnimator(new DefaultItemAnimator());

        emptyMessageLabel.setText("There are no suppliers to show.\nClick to refresh.");
    }

    @Override
    protected void refresh() {
        super.refresh();

        fetchSuppliers();
    }

    private void populateRecyclerView() {
        if (supplierAdapter == null) {
            supplierAdapter = new SupplierAdapter(this, mSuppliers);
            supplierAdapter.setSupplierListener(new SupplierAdapter.SupplierListener() {
                @Override
                public void onSupplierSelected(Supplier supplier) {
                    mSupplierDialog = new SupplierDialog(
                            SuppliersActivity.this,
                            supplier,
                            new SupplierDialog.SupplierDialogListener() {
                                @Override
                                public void onDelete(String supplierID) {
                                    deleteSupplier(supplierID);
                                }

                                @Override
                                public void onEdit(String supplierID) {
                                    editSupplier(supplierID);
                                }
                            });
                    mSupplierDialog.show();
                }

                @Override
                public void onEditSupplier(String supplierID) {
                    editSupplier(supplierID);
                }

                @Override
                public void onDeleteSupplier(String supplierID) {
                    deleteSupplier(supplierID);
                }
            });
            dataRecyclerView.setAdapter(supplierAdapter);
        } else
            supplierAdapter.animateTo(mSuppliers);
    }

    private void editSupplier(String supplierID) {
        Intent i = new Intent(SuppliersActivity.this, EditFournisseurActivity.class);
        i.putExtra("id", supplierID);
        startActivity(i);
    }

    private void deleteSupplier(String supplierID) {
        DialogUtil.showMutliDialog(
                SuppliersActivity.this,
                "Remove supplier",
                "Are you sure that you want to remove this supplier from your suppliers' list ?",
                "Yes",
                (dialog, which) -> sendHTTPRequest(
                        PhpAPI.removeFournisseur,
                        JSONUtils.bundleIDToJSON(supplierID),
                        Method.POST,
                        new DeleteSupplierListener()),
                "No", null);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * Toggles the visibility of the RecyclerView & the empty view associated with it.
     */
    private void toggleRecyclerviewState() {
        emptyLayout.setVisibility(mSuppliers.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        dataRecyclerView.setVisibility(mSuppliers.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
    }

    class DeleteSupplierListener implements RequestListener {

        @Override
        public void onRequestSucceeded(JSONObject response, int status) {
            if (status == 1) {
                runOnUiThread(() -> {
                    if (mSupplierDialog.isShowing())
                        mSupplierDialog.dismiss();

                    Toast.makeText(SuppliersActivity.this, "Supplier removed.",
                            Toast.LENGTH_LONG).show();
                    refresh();
                });

            } else if (status == 0) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                        "There was an error while processing your request.",
                        Toast.LENGTH_LONG).show());
            }
        }

        @Override
        public void onRequestFailed() {
            runOnUiThread(() -> Toast.makeText(SuppliersActivity.this,
                    "Unknown error", Toast.LENGTH_LONG).show());
        }
    }
}
