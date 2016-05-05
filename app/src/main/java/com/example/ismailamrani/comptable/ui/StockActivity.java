package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.StockAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.customitems.OGActionBar.SearchListener;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.Products;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StockActivity extends ColoredStatusBarActivity
        implements OGActionBarInterface, SearchListener,
        SearchView.OnQueryTextListener {

    private static final int REQUEST_ADD_PRODUCT = 100;

    private List<Product> mProducts;

    private StockAdapter stockAdapter;

    private OkHttpClient client = new OkHttpClient();

    /**
     * The stock's products list.
     */
    @Bind(R.id.stockRecyclerView)
    RecyclerView stockRecyclerView;

    /**
     * The view to be displayed in case there were no products in store.
     */
    @Bind(R.id.emptyLayout)
    RelativeLayout emptyView;

    /**
     * The view to be displayed in case a network error occur.
     */
    @Bind(R.id.errorLayout)
    RelativeLayout errorLayout;

    @Bind(R.id.stockProgressbar)
    ProgressBar stockProgressbar;

    @Bind(R.id.searchView)
    SearchView searchView;

    @Bind(R.id.searchCardView)
    CardView searchCardView;

    @Bind(R.id.actionBarContainer)
    RelativeLayout actionBarContainer;

    @Bind(R.id.MyActionBar)
    OGActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);

        setupActionBar();
        setupRecyclerView();
        setupSearchView();
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

        fetchStockProducts(PhpAPI.getStock, null);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(this);
    }

    public void onSearchDismiss(View view) {
        toggleSearchViewVisibility(View.GONE); // Hide search view
    }

    /**
     * @param visibility of the Search view
     */
    public void toggleSearchViewVisibility(int visibility) {
        int actionBarVisibility;
        if (visibility == View.VISIBLE)
            actionBarVisibility = View.GONE;
        else
            actionBarVisibility = View.VISIBLE;

        actionBarContainer.setVisibility(actionBarVisibility);
        searchCardView.setVisibility(visibility);
    }

    @Override
    public void onBackPressed() {
        if (searchCardView.getVisibility() == View.VISIBLE)
            toggleSearchViewVisibility(View.GONE);
        else
            super.onBackPressed();
    }

    @Override
    public void onMenuPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onAddPressed() {
        startActivityForResult(new Intent(this, AddProductActivity.class),
                REQUEST_ADD_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_PRODUCT:
                if (resultCode == ResultCodes.PRODUCT_ADDED) {
                    // A product has been successfully added
                    fetchStockProducts(PhpAPI.getStock, null);
                }
                break;
        }
    }

    void fetchStockProducts(String url, JSONObject userCredentials) {
        Request request = PhpAPI.createHTTPRequest(userCredentials, url, Method.GET);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(() -> {
                            errorLayout.setVisibility(View.VISIBLE);
                            stockProgressbar.setVisibility(View.INVISIBLE);
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(res);
                            mProducts = Product.parseProducts(
                                    obj.getJSONArray("products"));
                            runOnUiThread(() -> {
                                toggleRecyclerviewState();
                                populateRecyclerView();

                                stockProgressbar.setVisibility(View.INVISIBLE);
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void populateRecyclerView() {
        if (stockAdapter == null) {
            stockAdapter = new StockAdapter(this, mProducts);
            stockRecyclerView.setAdapter(stockAdapter);
        } else
            stockAdapter.animateTo(mProducts);
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

    /**
     * Invoked when the 'Search' menu item of the custom action bar is clicked.
     */
    @Override
    public void onSearchPressed() {
        actionBarContainer.setVisibility(View.GONE);
        searchCardView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Product> filteredContacts = Products.filter(mProducts, newText);
        stockAdapter.animateTo(filteredContacts);
        stockRecyclerView.scrollToPosition(0);
        return true;
    }
}
