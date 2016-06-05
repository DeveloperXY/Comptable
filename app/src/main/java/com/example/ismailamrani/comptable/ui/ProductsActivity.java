package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ProductsAdapter;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.base.RefreshableActivity;
import com.example.ismailamrani.comptable.utils.decorations.SpacesItemDecoration;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.parsing.ListComparison;
import com.example.ismailamrani.comptable.utils.ui.ActivityTransition;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Ismail Amrani on 23/03/2016.
 * Altered by Mohammed Aouf ZOUAG on 04/06/2016.
 */
public class ProductsActivity extends RefreshableActivity {

    private static final int REQUEST_ADD_PRODUCT = 1;
    private static final int REQUEST_UPDATE_PRODUCT = 2;

    private List<Product> mProducts;
    private ProductsAdapter mProductsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
        setupRecyclerView();
        setupSwipeRefresh();

        fetchProducts();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle(getString(R.string.products));
    }

    @Override
    protected void setupRecyclerView() {
        super.setupRecyclerView();

        mProducts = new ArrayList<>();
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataRecyclerView.addItemDecoration(new SpacesItemDecoration(4));
        emptyMessageLabel.setText(R.string.no_data_to_show);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_PRODUCT:
            case REQUEST_UPDATE_PRODUCT:
                switch (resultCode) {
                    case ResultCodes.PRODUCT_ADDED:
                    case ResultCodes.PRODUCT_UPDATED:
                        refresh();
                        break;
                }
                break;
        }
    }

    public void onErrorViewPressed(View view) {
        refresh();
    }

    private void populateRecyclerView() {
        if (mProductsAdapter == null) {
            mProductsAdapter = new ProductsAdapter(this, mProducts);
            mProductsAdapter.setProductListener(new ProductsAdapter.ProductListener() {
                @Override
                public void onDeleteProduct(String url, JSONObject params, Method method) {
                    mLoadingDialog.show();
                    sendHTTPRequest(url, params, method, new DeleteRequestListener());
                }

                @Override
                public void onEditProduct(Intent intent, ImageView productImage, TextView productLabel) {
                    ActivityTransition.startActivityForResultWithMultipleSharedElements(
                            ProductsActivity.this, intent, REQUEST_UPDATE_PRODUCT,
                            Pair.create((View) productLabel, "productLabelAnim"),
                            Pair.create((View) productImage, "productImageAnim")
                    );
                }

                @Override
                public void onViewProduct(Intent intent, ImageView productImage, TextView productLabel) {
                    ActivityTransition.startActivityWithMultipleSharedElements(
                            ProductsActivity.this, intent,
                            Pair.create((View) productLabel, "productLabelAnim"),
                            Pair.create((View) productImage, "productImageAnim")
                    );
                }
            });
            dataRecyclerView.setAdapter(mProductsAdapter);
        } else
            mProductsAdapter.animateTo(mProducts);
    }

    private void toggleRecyclerviewState() {
        emptyLayout.setVisibility(mProducts.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        dataRecyclerView.setVisibility(mProducts.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.PRODUCTS;
    }

    private void fetchProducts() {
        int localeID = mDatabaseAdapter.getCurrentLocaleID();
        JSONObject data = JSONUtils.bundleLocaleIDToJSON(localeID);

        sendHTTPRequest(PhpAPI.getProduit, data, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        try {
                            JSONArray productsArray = response.getJSONArray("produit");
                            final List<Product> products = Product.parseProducts(productsArray);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!ListComparison.areEqual(mProducts, products)) {
                                        mProducts = products;
                                        dataRecyclerView.scrollToPosition(0);
                                        populateRecyclerView();
                                    }

                                    stopSwipeRefresh();
                                    toggleRecyclerviewState();
                                    progressBar.setVisibility(View.INVISIBLE);
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
                });
    }

    @Override
    public void onAddPressed() {
        startActivityForResult(new Intent(this, AddProductActivity.class), REQUEST_ADD_PRODUCT);
    }

    @Override
    protected void refresh() {
        fetchProducts();
    }

    class DeleteRequestListener extends SuccessRequestListener {
        @Override
        public void onRequestSucceeded(JSONObject response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ProductsActivity.this, R.string.product_removed,
                            Toast.LENGTH_LONG).show();
                    refresh();
                }
            });
        }

        @Override
        public void onRequestFailed(int status, JSONObject response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ProductsActivity.this, R.string.unknown_error,
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
