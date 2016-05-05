package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.StockAdapter;
import com.example.ismailamrani.comptable.adapters.orders.SaleOrdersAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.utils.Method;
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

public class SaleOrdersActivty extends AppCompatActivity implements OGActionBarInterface {

    private static final int REQUEST_ADD_SALE_ORDER = 100;

    private List<Order> mOrders;
    private SaleOrdersAdapter ordersAdapter;
    private OkHttpClient client = new OkHttpClient();

    @Bind(R.id.MyActionBar)
    OGActionBar mActionBar;

    /**
     * The orders' list.
     */
    @Bind(R.id.saleOrdersRecyclerView)
    RecyclerView saleOrdersRecyclerView;

    @Bind(R.id.emptyMessageLabel)
    TextView emptyMessageLabel;

    /**
     * The view to be displayed in case there were no sale orders to show.
     */
    @Bind(R.id.emptyLayout)
    RelativeLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_orders_activty);
        ButterKnife.bind(this);

        setupActionBar();
        setupRecyclerView();
        toggleRecyclerviewState();
        populateRecyclerView();
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
        saleOrdersRecyclerView.setHasFixedSize(true);
        saleOrdersRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Specify the message of the empty view
        emptyMessageLabel.setText("There are no sale orders to show.");

        fetchSaleOrders(PhpAPI.getSaleOrder, null);
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

    private void populateRecyclerView() {
        if (ordersAdapter == null) {
            ordersAdapter = new SaleOrdersAdapter(this, mOrders);
            saleOrdersRecyclerView.setAdapter(ordersAdapter);
        } else
            ordersAdapter.animateTo(mOrders);
    }

    /**
     * Toggles the visibility of the RecyclerView & the empty view associated with it.
     */
    private void toggleRecyclerviewState() {
        emptyView.setVisibility(mOrders.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        saleOrdersRecyclerView.setVisibility(mOrders.size() == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    void fetchSaleOrders(String url, JSONObject data) {
        Request request = PhpAPI.createHTTPRequest(data, url, Method.GET);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(() -> {
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
                                });
                            }
                            else
                                runOnUiThread(() -> Toast.makeText(SaleOrdersActivty.this,
                                        "Error while retrieving sale orders",
                                        Toast.LENGTH_LONG).show());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
