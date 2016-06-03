package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.DatabaseAdapter;
import com.example.ismailamrani.comptable.adapters.StockAdapter;
import com.example.ismailamrani.comptable.barcodescanner.IntentIntegrator;
import com.example.ismailamrani.comptable.barcodescanner.IntentResult;
import com.example.ismailamrani.comptable.customitems.OGActionBar.SearchListener;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.base.RefreshableActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.parsing.ListComparison;
import com.example.ismailamrani.comptable.utils.parsing.Products;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StockActivity extends RefreshableActivity
        implements SearchListener, SearchView.OnQueryTextListener {

    private static final int REQUEST_ADD_PRODUCT = 100;

    private List<Product> mProducts;
    private StockAdapter stockAdapter;
    private int currentLocaleID;

    @Bind(R.id.searchView)
    SearchView searchView;

    @Bind(R.id.searchLayoutView)
    RelativeLayout searchLayoutView;

    @Bind(R.id.actionBarContainer)
    RelativeLayout actionBarContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
        setupRecyclerView();
        setupSearchView();
        setupSwipeRefresh();

        currentLocaleID = DatabaseAdapter.getInstance(this)
                .getCurrentLocaleID();
        refresh(); // Must be called last
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setSearchListener(this);
        mActionBar.setTitle("Stock");
        mActionBar.isSearchable(true);
    }

    /**
     * Initial setup of the stock's RecyclerView.
     */
    @Override
    protected void setupRecyclerView() {
        super.setupRecyclerView();

        mProducts = new ArrayList<>();
        dataRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        );
        emptyMessageLabel.setText("There are no products to show.\nClick to refresh.");
    }

    @Override
    protected void refresh() {
        fetchStockProducts(PhpAPI.getStock, JSONUtils.bundleLocaleIDToJSON(currentLocaleID));
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            toggleSearchViewVisibility(View.GONE); // Hide search view
            stockAdapter.animateTo(mProducts);
        });
    }

    @Override
    public void onBackPressed() {
        if (searchLayoutView.getVisibility() == View.VISIBLE)
            toggleSearchViewVisibility(View.GONE);
        else
            super.onBackPressed();
    }

    @Override
    public void onAddPressed() {
        startActivityForResult(new Intent(this, AddProductActivity.class),
                REQUEST_ADD_PRODUCT);
    }

    /**
     * Invoked when the 'Search' menu item of the custom action bar is clicked.
     */
    @Override
    public void onSearchPressed() {
        if (mProducts.size() != 0)
            toggleSearchViewVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Product> filteredProducts = Products.filter(mProducts, newText);
        if (stockAdapter != null)
            stockAdapter.animateTo(filteredProducts);
        return true;
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.STOCK;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_PRODUCT:
                if (resultCode == ResultCodes.PRODUCT_ADDED) {
                    // A product has been successfully added
                    refresh();
                }
                break;
            default:
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (scanningResult != null) {
                    String scannedBarcode = scanningResult.getContents();
                    Intent intent = new Intent(this, ProductDetailsActivity.class);
                    Product requestedProduct;

                    try {
                        requestedProduct = Stream.of(mProducts)
                                .filter(product -> product.getCodeBarre().equals(scannedBarcode))
                                .findFirst()
                                .get();
                    }
                    catch (NoSuchElementException e) {
                        requestedProduct = null;
                    }

                    if (requestedProduct == null) {
                        Snackbar.make(getWindow().getDecorView(),
                                "No product was found matching your query.",
                                Snackbar.LENGTH_LONG).show();
                    }
                    else {
                        intent.putExtra("product", requestedProduct);
                        startActivity(intent);
                    }
                }
        }
    }

    public void onErrorViewPressed(View view) {
        refresh();
    }

    void fetchStockProducts(String url, JSONObject data) {
        sendHTTPRequest(url, data, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        try {
                            List<Product> products = Product.parseProducts(
                                    response.getJSONArray("products"));

                            if (ListComparison.areEqual(mProducts, products))
                                runOnUiThread(this::handleDataChange);
                            else {
                                mProducts = products;
                                runOnUiThread(() -> {
                                    handleDataChange();
                                    populateRecyclerView();
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestFailed(int status, JSONObject response) {

                    }

                    @Override
                    public void onNetworkError() {
                        runOnUiThread(() -> handleRequestError());
                    }

                    private void handleDataChange() {
                        toggleRecyclerviewState();
                        progressBar.setVisibility(View.INVISIBLE);
                        stopSwipeRefresh();
                    }
                });
    }

    private void populateRecyclerView() {
        if (stockAdapter == null) {
            stockAdapter = new StockAdapter(this, mProducts);
            dataRecyclerView.setAdapter(stockAdapter);
        } else
            stockAdapter.animateTo(mProducts);
    }

    /**
     * @param visibility of the Search view
     */
    public void toggleSearchViewVisibility(int visibility) {
        int actionBarVisibility = visibility == View.VISIBLE ? View.GONE : View.VISIBLE;

        actionBarContainer.setVisibility(actionBarVisibility);
        searchLayoutView.setVisibility(visibility);

        if (visibility == View.GONE) {
            // Empty the content of the search view if dismissed
            searchView.setQuery("", false);
        }
    }

    /**
     * Toggles the visibility of the RecyclerView & the empty view associated with it.
     */
    private void toggleRecyclerviewState() {
        /* Set the visibility of the empty view & the stockRecyclerView
        based on the number of products in store.*/
        emptyLayout.setVisibility(mProducts.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        dataRecyclerView.setVisibility(mProducts.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
    }

    public void onScanFABClicked(View view) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }
}
