package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.StockAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.SearchListener;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.ListComparison;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.Products;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StockActivity extends ColoredStatusBarActivity
        implements SearchListener, SearchView.OnQueryTextListener {

    private static final int REQUEST_ADD_PRODUCT = 100;

    private List<Product> mProducts;
    private StockAdapter stockAdapter;
    private int currentLocaleID;

    /**
     * The stock's products list.
     */
    @Bind(R.id.stockRecyclerView)
    RecyclerView stockRecyclerView;

    /**
     * The view to be displayed in case there were no products in store.
     */
    @Bind(R.id.emptyStockLayout)
    RelativeLayout emptyView;

    @Bind(R.id.emptyMessageLabel)
    TextView emptyMessageLabel;

    /**
     * The view to be displayed in case a network error occur.
     */
    @Bind(R.id.errorStockLayout)
    RelativeLayout errorLayout;

    @Bind(R.id.progressBar)
    ProgressBar stockProgressbar;

    @Bind(R.id.searchView)
    SearchView searchView;

    @Bind(R.id.searchLayoutView)
    RelativeLayout searchLayoutView;

    @Bind(R.id.actionBarContainer)
    RelativeLayout actionBarContainer;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);

        setupActionBar();
        setupRecyclerView();
        setupSearchView();
        setupSwipeRefresh();

        currentLocaleID = DatabaseAdapter.getInstance(this)
                .getCurrentLocaleID();
        refresh(); // Must be called last
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setSearchListener(this);
        mActionBar.setTitle("Stock");
        mActionBar.isSearchable(true);
    }

    /**
     * Initial setup of the stock's RecyclerView.
     */
    private void setupRecyclerView() {
        mProducts = new ArrayList<>();
        stockRecyclerView.setHasFixedSize(true);
        stockRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        );
        emptyMessageLabel.setText("There are no products to show.\nClick to refresh.");
    }

    private void refresh() {
        if (!swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(true);

        fetchStockProducts(PhpAPI.getStock, JSONUtils.bundleLocaleIDToJSON(currentLocaleID));
    }

    private void stopSwipeRefresh() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
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

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipeRefresh1,
                R.color.swipeRefresh2,
                R.color.swipeRefresh3,
                R.color.swipeRefresh4
        );
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
        stockAdapter.animateTo(filteredProducts);
        return true;
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
        }
    }

    public void onErrorViewPressed(View view) {
        refresh();
    }

    void fetchStockProducts(String url, JSONObject data) {
        sendHTTPRequest(url, data, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        try {
                            List<Product> products = Product.parseProducts(
                                    response.getJSONArray("products"));

                            if (ListComparison.areEqual(mProducts, products))
                                runOnUiThread(() -> stopSwipeRefresh());
                            else {
                                mProducts = products;
                                runOnUiThread(() -> {
                                    toggleRecyclerviewState();
                                    populateRecyclerView();

                                    stockProgressbar.setVisibility(View.INVISIBLE);
                                    stopSwipeRefresh();
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> {
                            errorLayout.setVisibility(View.VISIBLE);
                            stockProgressbar.setVisibility(View.INVISIBLE);
                            stopSwipeRefresh();
                        });
                    }
                });
    }

    private void populateRecyclerView() {
        if (stockAdapter == null) {
            stockAdapter = new StockAdapter(this, mProducts);
            stockRecyclerView.setAdapter(stockAdapter);
        } else
            stockAdapter.animateTo(mProducts);

        checkIfStockIsEmpty();
    }

    private void checkIfStockIsEmpty() {
        if (mProducts.size() == 0) {
            Snackbar.make(getWindow().getDecorView(), "Your stock is empty.",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("Add a product", v -> onAddPressed()).show();
        }
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
        emptyView.setVisibility(mProducts.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        stockRecyclerView.setVisibility(mProducts.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
    }
}
