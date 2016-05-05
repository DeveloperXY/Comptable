package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.orders.OrdersAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.SpacesItemDecoration;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mohammed Aouf ZOUAG on 05/05/2016.
 */
public abstract class AbstractOrdersActivity extends AppCompatActivity
        implements OGActionBarInterface {

    protected List<Order> mOrders;
    protected OrdersAdapter ordersAdapter;
    protected OkHttpClient client = new OkHttpClient();

    @Bind(R.id.MyActionBar)
    OGActionBar mActionBar;

    /**
     * The orders' list.
     */
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.emptyMessageLabel)
    TextView emptyMessageLabel;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    /**
     * The view to be displayed in case a network error occur.
     */
    @Bind(R.id.errorLayout)
    RelativeLayout errorLayout;

    /**
     * The view to be displayed in case there were no orders to show.
     */
    @Bind(R.id.emptyLayout)
    RelativeLayout emptyView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    protected void setupUI() {
        setupSwipeRefresh();
        setupRecyclerView();
    }

    protected void setupActionBar(OGActionBarInterface listener, String title) {
        mActionBar.setActionBarListener(listener);
        mActionBar.setTitle(title);
    }

    protected void setupRecyclerView() {
        mOrders = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));
    }

    protected void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipeRefresh1,
                R.color.swipeRefresh2,
                R.color.swipeRefresh3,
                R.color.swipeRefresh4
        );
    }

    protected void stopSwipeRefresh() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Toggles the visibility of the RecyclerView & the empty view associated with it.
     */
    protected void toggleRecyclerviewState() {
        emptyView.setVisibility(mOrders.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        recyclerView.setVisibility(mOrders.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
    }

    public void onEmptyViewPressed(View view) {
        refresh();
        Log.i("REFRESH", "ON");
    }

    @Override
    public void onMenuPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void onDataChanged() {
        toggleRecyclerviewState();
        populateRecyclerView();

        progressBar.setVisibility(View.INVISIBLE);
        stopSwipeRefresh();
    }

    protected void fetchOrders(String url, JSONObject data) {
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
                                runOnUiThread(() -> onDataChanged());
                            } else
                                runOnUiThread(() -> Toast.makeText(
                                        AbstractOrdersActivity.this,
                                        "Error while retrieving purchase orders",
                                        Toast.LENGTH_LONG).show());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void populateRecyclerView() {
        if (ordersAdapter == null) {
            ordersAdapter = new OrdersAdapter(this, mOrders);
            recyclerView.setAdapter(ordersAdapter);
        } else
            ordersAdapter.animateTo(mOrders);
    }

    protected void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        Stream.of(emptyView, recyclerView, errorLayout)
                .forEach(v -> v.setVisibility(View.INVISIBLE));
    }
}
