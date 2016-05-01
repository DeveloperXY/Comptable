package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.StockAdapter;
import com.example.ismailamrani.comptable.customitems.ColorStatutBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StockActivity extends Activity implements OGActionBarInterface {

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

    @Bind(R.id.stockProgressbar)
    ProgressBar stockProgressbar;

    OGActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        new ColorStatutBar().ColorStatutBar(this);
        ButterKnife.bind(this);

        initializeUI();
        setupRecyclerView();
    }

    private void initializeUI() {
        mActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Stock");

        stockProgressbar.setIndeterminate(true);
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
                    // Product successfully added
                }
                break;
        }
    }

    /**
     * Initial setup of the stock's RecyclerView.
     */
    private void setupRecyclerView() {
        stockRecyclerView.setHasFixedSize(true);
        stockRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        );

        try {
            fetchStockProducts(PhpAPI.getStock, null);
        } catch (IOException e) {
            Toast.makeText(this, "An error occured when fetching stock products.",
                    Toast.LENGTH_LONG).show();
        }
    }

    void fetchStockProducts(String url, JSONObject userCredentials) throws IOException {
        Request request = PhpAPI.createHTTPRequest(userCredentials, url, Method.GET);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(() -> Toast.makeText(StockActivity.this,
                                call.request().toString(), Toast.LENGTH_LONG).show());
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
        stockAdapter = new StockAdapter(this, mProducts);
        stockRecyclerView.setAdapter(stockAdapter);
    }

    /**
     * Toggles the visibility of the RecyclerView & the empty view associated with it.
     */
    private void toggleRecyclerviewState() {
        /* Set the visibility of the empty view & the stockRecyclerView
        based on the number of products in store.*/
        emptyView.setVisibility(mProducts.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        stockRecyclerView.setVisibility(mProducts.size() == 0 ? View.INVISIBLE : View.VISIBLE);
    }
}
