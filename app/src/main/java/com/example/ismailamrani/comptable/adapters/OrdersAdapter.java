package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Order;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 30/04/2016.
 */
public class OrdersAdapter extends BaseSearchAdapter<OrdersAdapter.OrdersViewHolder, Order> {

    private OrdersListener listener;

    public OrdersAdapter(Context context, List<Order> orders) {
        super(context, orders);
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_row, viewGroup, false);

        return new OrdersViewHolder(v);
    }

    public void setOrdersListener(OrdersListener listener) {
        this.listener = listener;
    }

    public interface OrdersListener {
        void onOrderSelected(Order order);
    }

    public class OrdersViewHolder extends BinderViewHolder<Order> {

        TextView orderIDLabel;
        TextView plusSignLabel;
        RelativeLayout statusColorLayout;

        public OrdersViewHolder(View v) {
            super(v);
            orderIDLabel = (TextView) v.findViewById(R.id.orderIDLabel);
            plusSignLabel = (TextView) v.findViewById(R.id.plusSignLabel);
            statusColorLayout = (RelativeLayout) v.findViewById(R.id.statusColorLayout);

            v.setOnClickListener(view -> {
                if (listener != null)
                    listener.onOrderSelected(mItems.get(getLayoutPosition()));
            });
        }

        @Override
        public void bind(Order order) {
            orderIDLabel.setText(order.getFactureID());
            statusColorLayout.setBackgroundColor(
                    Color.parseColor(order.getFacture() == 1 ? "#2E7D32" : "#D50000"));
        }
    }
}