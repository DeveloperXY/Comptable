package com.example.ismailamrani.comptable.ui.orders.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.AbstractOrdersActivity;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import butterknife.ButterKnife;

public class SaleOrdersActivity extends AbstractOrdersActivity {

    private static final int REQUEST_ADD_SALE_ORDER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_orders_activity);
        ButterKnife.bind(this);

        setupUI();
        refresh();
    }

    @Override
    protected void setupUI() {
        super.setupUI();

        setupActionBar(this, "Commandes ventes");
        // Specify the message of the empty view
        emptyMessageLabel.setText("There are no sale orders to show.\nClick to refresh.");
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

    @Override
    protected void refresh() {
        super.refresh();
        fetchOrders(PhpAPI.getSaleOrder, null);
    }
}
