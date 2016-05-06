package com.example.ismailamrani.comptable.ui.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.orders.adapters.OrdersAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.utils.SpacesItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.OkHttpClient;

/**
 * Created by Mohammed Aouf ZOUAG on 05/05/2016.
 */
public abstract class AbstractOrdersActivity extends ColoredStatusBarActivity {

    protected List<Order> mOrders;
    protected OrdersAdapter ordersAdapter;
    protected OkHttpClient client = new OkHttpClient();

    @Bind(R.id.MyActionBar)
    protected OGActionBar mActionBar;

    /**
     * The orders' list.
     */
    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @Bind(R.id.emptyMessageLabel)
    protected TextView emptyMessageLabel;

    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;

    /**
     * The view to be displayed in case a network error occur.
     */
    @Bind(R.id.errorLayout)
    protected RelativeLayout errorLayout;

    /**
     * The view to be displayed in case there were no orders to show.
     */
    @Bind(R.id.emptyLayout)
    protected RelativeLayout emptyView;

    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

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

    public void onErrorViewPressed(View view) {
        refresh();
    }

    protected void onDataChanged() {
        toggleRecyclerviewState();
        populateRecyclerView();

        progressBar.setVisibility(View.INVISIBLE);
        stopSwipeRefresh();
    }

    protected void fetchOrders(String url, JSONObject data) {
        sendHTTPRequest(client, url, data, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 1) {
                            try {
                                mOrders = Order.parseOrders(
                                        response.getJSONArray("orders"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(() -> onDataChanged());
                        } else
                            Toast.makeText(AbstractOrdersActivity.this,
                                    "Error while retrieving purchase orders",
                                    Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRequestFailed() {
                        errorLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        stopSwipeRefresh();
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
        if (!swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(true);

        Stream.of(emptyView, recyclerView, errorLayout)
                .forEach(v -> v.setVisibility(View.INVISIBLE));
    }
}
