package com.example.ismailamrani.comptable.adapters.orders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Order;

/**
 * Created by Mohammed Aouf ZOUAG on 05/05/2016.
 */
public class OrdersViewHolder extends RecyclerView.ViewHolder {

    TextView orderIDLabel;
    TextView plusSignLabel;

    public OrdersViewHolder(View v) {
        super(v);
        orderIDLabel = (TextView) v.findViewById(R.id.orderIDLabel);
        plusSignLabel = (TextView) v.findViewById(R.id.plusSignLabel);
    }

    public void bind(Order order) {
        orderIDLabel.setText(order.getFactureID());
    }
}