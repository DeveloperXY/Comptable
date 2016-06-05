package com.example.ismailamrani.comptable.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.SupplierAdapter;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.ui.base.RefreshableActivity;
import com.example.ismailamrani.comptable.ui.dialogs.SupplierDialog;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.ui.ActivityTransition;
import com.example.ismailamrani.comptable.utils.ui.DialogUtil;
import com.example.ismailamrani.comptable.utils.decorations.GridSpacingItemDecoration;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.parsing.ListComparison;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
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

    private static final int REQUEST_ADD_SUPPLIER = 10;
    private static final int REQUEST_UPDATE_SUPPLIER = 11;

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

        mActionBar.setTitle(getString(R.string.suppliers));
    }

    @Override
    public void onAddPressed() {
        ActivityTransition.startActivityForResultWithSharedElement(
                this, new Intent(this, AlterSupplierActivity.class), actionbarImage,
                "menuAnim", REQUEST_ADD_SUPPLIER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_SUPPLIER:
            case REQUEST_UPDATE_SUPPLIER:
                switch (resultCode) {
                    case ResultCodes.SUPPLIER_CREATED:
                    case ResultCodes.SUPPLIER_UPDATED:
                        refresh();
                        break;
                }
                break;
        }
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
                    public void onRequestSucceeded(JSONObject response) {
                        try {
                            JSONArray suppliers = response.getJSONArray("fournisseur");
                            List<Supplier> supplierList = Supplier.parseSuppliers(suppliers);

                            if (ListComparison.areEqual(mSuppliers, supplierList))
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        handleDataChange();
                                    }
                                });
                            else {
                                mSuppliers = supplierList;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        handleDataChange();
                                        populateRecyclerView();
                                    }
                                });
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    populateRecyclerView();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    dataRecyclerView.requestLayout();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestFailed(int status, JSONObject response) {

                    }

                    @Override
                    public void onNetworkError() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleRequestError();
                            }
                        });
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
        mSuppliers = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        dataRecyclerView.setLayoutManager(layoutManager);
        dataRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        dataRecyclerView.setItemAnimator(new DefaultItemAnimator());

        emptyMessageLabel.setText(R.string.no_suppliers_to_show);
    }

    @Override
    protected void refresh() {
        fetchSuppliers();
    }

    private void populateRecyclerView() {
        if (supplierAdapter == null) {
            supplierAdapter = new SupplierAdapter(this, mSuppliers);
            supplierAdapter.setSupplierListener(new SupplierAdapter.SupplierListener() {
                @Override
                public void onSupplierSelected(Supplier supplier) {
                    mSupplierDialog = new SupplierDialog(SuppliersActivity.this, supplier);
                    mSupplierDialog.show();
                }

                @Override
                public void onEditSupplier(Supplier supplier, ImageView itemImage) {
                    Intent i = new Intent(SuppliersActivity.this, AlterSupplierActivity.class);
                    i.putExtra("supplier", supplier);
                    i.putExtra("isUpdating", true);
                    ActivityTransition.startActivityForResultWithSharedElement(
                            SuppliersActivity.this, i, itemImage, "menuAnim",
                            REQUEST_UPDATE_SUPPLIER);
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

    private void deleteSupplier(final String supplierID) {
        DialogUtil.showMutliDialog(
                SuppliersActivity.this,
                getString(R.string.remove_supplier),
                getString(R.string.question_remove_this_supplier),
                getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendHTTPRequest(
                                PhpAPI.removeFournisseur,
                                JSONUtils.bundleIDToJSON(supplierID),
                                Method.POST,
                                new DeleteSupplierListener());
                    }
                },
                getString(R.string.no), null);
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

    class DeleteSupplierListener extends SuccessRequestListener {

        @Override
        public void onRequestSucceeded(JSONObject response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SuppliersActivity.this, R.string.supplier_removed,
                            Toast.LENGTH_LONG).show();
                    refresh();
                }
            });
        }
    }
}
