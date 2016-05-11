package com.example.ismailamrani.comptable.ui.orders;

import android.content.Intent;
import android.os.Bundle;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.OrderEnum;

import butterknife.ButterKnife;

public class OrdersActivity extends ColoredStatusBarActivity {

    private OrderEnum currentOrderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);

        setupOrdersType();
    }

    /**
     * Retrieves the type of orders to be currently displayed.
     */
    private void setupOrdersType() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                String incoming = data.getString("orderType");
                currentOrderType = "SALE".equals(incoming) ? OrderEnum.SALE : OrderEnum.PURCHASE;
            }
        }
    }
}
