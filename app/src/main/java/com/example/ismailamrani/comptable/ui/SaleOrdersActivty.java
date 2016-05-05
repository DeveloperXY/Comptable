package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.StockAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SaleOrdersActivty extends AppCompatActivity implements OGActionBarInterface {

    private static final int REQUEST_ADD_SALE_ORDER = 100;

    private List<Order> mOrders;
    private StockAdapter ordersAdapter;

    @Bind(R.id.MyActionBar)
    OGActionBar mActionBar;

    /**
     * The orders' list.
     */
    @Bind(R.id.saleOrdersRecyclerView)
    RecyclerView saleOrdersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_orders_activty);
        ButterKnife.bind(this);

        setupActionBar();
        setupRecyclerView();
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
}
