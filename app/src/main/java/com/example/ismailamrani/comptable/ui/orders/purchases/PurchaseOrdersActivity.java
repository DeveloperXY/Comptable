package com.example.ismailamrani.comptable.ui.orders.purchases;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.AbstractOrdersActivity;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import butterknife.ButterKnife;

public class PurchaseOrdersActivity extends AbstractOrdersActivity {

    private static final int REQUEST_ADD_PURCHASE_ORDER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_orders_activity);
        ButterKnife.bind(this);

        setupUI();
        refresh();
    }

    @Override
    protected void setupUI() {
        super.setupUI();

        setupActionBar(this, "Commandes achats");
        // Specify the message of the empty view
        emptyMessageLabel.setText("There are no purchase orders to show.");
    }

    @Override
    public void onAddPressed() {
        startActivityForResult(new Intent(this, PurchasesActivity.class),
                REQUEST_ADD_PURCHASE_ORDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_PURCHASE_ORDER:
                if (resultCode == ResultCodes.PURCHASE_ORDER_CREATED) {
                    // A purchase order was created
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
        fetchOrders(PhpAPI.getPurchaseOrder, null);
    }
}
