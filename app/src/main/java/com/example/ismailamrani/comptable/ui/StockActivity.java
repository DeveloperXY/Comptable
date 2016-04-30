package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.ColorStatutBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Product;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StockActivity extends Activity implements OGActionBarInterface {

    private List<Product> mProducts;

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

    }
}
