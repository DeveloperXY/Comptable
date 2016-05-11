package com.example.ismailamrani.comptable.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.utils.OrderEnum;

import butterknife.ButterKnife;

public class OrdersActivity extends AbstractOrdersActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                String incoming = data.getString("orderType");
                Log.i("INCOMING", "#" + incoming);
                OrderEnum orderEnum = "SALE".equals(incoming) ? OrderEnum.SALE : OrderEnum.PURCHASE;


            }
        }
    }
}
