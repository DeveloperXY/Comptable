package com.example.ismailamrani.comptable.ui.orders.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.ui.orders.sales.SaleOrdersActivity;

/**
 * Created by Mohammed Aouf ZOUAG on 05/05/2016.
 */
public class OrdersViewHolder extends RecyclerView.ViewHolder {

    TextView orderIDLabel;
    TextView plusSignLabel;
    RelativeLayout statusColorLayout;

    public OrdersViewHolder(Context context, View v) {
        super(v);
        orderIDLabel = (TextView) v.findViewById(R.id.orderIDLabel);
        plusSignLabel = (TextView) v.findViewById(R.id.plusSignLabel);
        statusColorLayout = (RelativeLayout) v.findViewById(R.id.statusColorLayout);

        v.setOnClickListener(view -> Toast.makeText(context,
                context instanceof SaleOrdersActivity ? "Sales" : "Purchases",
                Toast.LENGTH_LONG).show());
    }

    public void bind(Order order) {
        orderIDLabel.setText(order.getFactureID());
        statusColorLayout.setBackgroundColor(
                Color.parseColor(order.getFacture() == 1 ? "#2E7D32" : "#D50000"));
    }
}