package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SaleOrdersActivty extends AppCompatActivity implements OGActionBarInterface {

    private static final int REQUEST_ADD_SALE_ORDER = 100;

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
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Commandes ventes");
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
