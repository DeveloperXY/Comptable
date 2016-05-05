package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.orders.OrdersAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.utils.SpacesItemDecoration;
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

public class SaleOrdersActivity extends AbstractOrdersActivity
        implements OGActionBarInterface {

    private static final int REQUEST_ADD_SALE_ORDER = 100;

    private List<Order> mOrders;
    private OrdersAdapter ordersAdapter;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_orders_activity);
        ButterKnife.bind(this);

        setupActionBar();
        setupRecyclerView();
        setupSwipeRefresh();
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Commandes ventes");
    }

    /**
     * Initial setup of the stock's RecyclerView.
     */
    private void setupRecyclerView() {
        mOrders = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));

        // Specify the message of the empty view
        emptyMessageLabel.setText("There are no sale orders to show.");

        refresh();
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
    public void onMenuPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onAddPressed() {
        startActivityForResult(new Intent(this, SalesActivity.class),
                REQUEST_ADD_SALE_ORDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_SALE_ORDER:
                if (resultCode == ResultCodes.SALE_ORDER_CREATED) {
                    // A sale order was created
                    Snackbar.make(getWindow().getDecorView(),
                            "Commande créée avec succès.", Snackbar.LENGTH_LONG)
                            .show();
                    refresh();
                }
                break;
        }
    }

    public void refresh() {
        fetchSaleOrders(PhpAPI.getSaleOrder, null);
    }

    private void populateRecyclerView() {
        if (ordersAdapter == null) {
            ordersAdapter = new OrdersAdapter(this, mOrders);
            recyclerView.setAdapter(ordersAdapter);
        } else
            ordersAdapter.animateTo(mOrders);
    }

    /**
     * Toggles the visibility of the RecyclerView & the empty view associated with it.
     */
    private void toggleRecyclerviewState() {
        emptyView.setVisibility(mOrders.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        recyclerView.setVisibility(mOrders.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
    }

    void fetchSaleOrders(String url, JSONObject data) {
        Request request = PhpAPI.createHTTPRequest(data, url, Method.GET);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(() -> {
                            errorLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            stopSwipeRefresh();
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(res);
                            int status = obj.getInt("success");

                            if (status == 1) {
                                mOrders = Order.parseOrders(
                                        obj.getJSONArray("orders"));
                                runOnUiThread(() -> {
                                    toggleRecyclerviewState();
                                    populateRecyclerView();

                                    progressBar.setVisibility(View.INVISIBLE);
                                    stopSwipeRefresh();
                                });
                            }
                            else
                                runOnUiThread(() -> Toast.makeText(SaleOrdersActivity.this,
                                        "Error while retrieving sale orders",
                                        Toast.LENGTH_LONG).show());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void stopSwipeRefresh() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }
}
