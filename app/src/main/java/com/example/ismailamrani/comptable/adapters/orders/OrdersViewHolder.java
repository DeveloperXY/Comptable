package com.example.ismailamrani.comptable.adapters.orders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.ui.SaleOrdersActivity;

/**
 * Created by Mohammed Aouf ZOUAG on 05/05/2016.
 */
public class OrdersViewHolder extends RecyclerView.ViewHolder {

    TextView orderIDLabel;
    TextView plusSignLabel;

    public OrdersViewHolder(Context context, View v) {
        super(v);
        orderIDLabel = (TextView) v.findViewById(R.id.orderIDLabel);
        plusSignLabel = (TextView) v.findViewById(R.id.plusSignLabel);

        v.setOnClickListener(view -> Toast.makeText(context,
                context instanceof SaleOrdersActivity ? "Sales" : "Purchases",
                Toast.LENGTH_LONG).show());
    }

    public void bind(Order order) {
        orderIDLabel.setText(order.getFactureID());
    }
}