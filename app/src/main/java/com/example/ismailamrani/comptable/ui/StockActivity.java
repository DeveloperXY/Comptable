package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.StockAdapter;
import com.example.ismailamrani.comptable.customitems.ColorStatutBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Product;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StockActivity extends Activity implements OGActionBarInterface {

    private List<Product> mProducts;
    private StockAdapter stockAdapter;

    /**
     * The stock's products list.
     */
    @Bind(R.id.stockRecyclerView)
    RecyclerView stockRecyclerView;
    OGActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        new ColorStatutBar().ColorStatutBar(this);
        ButterKnife.bind(this);

        mActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Stock");

        setupRecyclerView();
    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }

    /**
     * Initial setup of the stock's RecyclerView.
     */
    private void setupRecyclerView() {
        stockRecyclerView.setHasFixedSize(true);
        stockRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        );

        Product product1 = new Product(1,"product1",12d, 13d, "123456", "", 215, 0, "");
        Product product2 = new Product(1,"product2",12d, 13d, "123456", "", 20, 0, "");
        Product product3 = new Product(1,"product3",12d, 13d, "123456", "", 110, 0, "");
        List<Product> products = Arrays.asList(product1, product2, product3);

        stockAdapter = new StockAdapter(this, products);
        stockRecyclerView.setAdapter(stockAdapter);
    }
}
