package com.example.ismailamrani.comptable.ui.orders.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 30/04/2016.
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private Context mContext;
    private List<Order> mOrders;
    private OrdersListener listener;

    public OrdersAdapter(Context context, List<Order> orders) {
        mContext = context;
        mOrders = new ArrayList<>(orders);
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_row, viewGroup, false);

        return new OrdersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrdersViewHolder holder, int position) {
        Order order = mOrders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public Order removeItem(int position) {
        final Order order = mOrders.remove(position);
        notifyItemRemoved(position);
        return order;
    }

    public void addItem(int position, Order order) {
        mOrders.add(position, order);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Order order = mOrders.remove(fromPosition);
        mOrders.add(toPosition, order);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Order> orders) {
        applyAndAnimateRemovals(orders);
        applyAndAnimateAdditions(orders);
        applyAndAnimateMovedItems(orders);
    }

    private void applyAndAnimateRemovals(List<Order> newOrders) {
        for (int i = mOrders.size() - 1; i >= 0; i--) {
            final Order order = mOrders.get(i);
            if (!newOrders.contains(order))
                removeItem(i);
        }
    }

    private void applyAndAnimateAdditions(List<Order> newOrders) {
        for (int i = 0, count = newOrders.size(); i < count; i++) {
            final Order order = newOrders.get(i);
            if (!mOrders.contains(order))
                addItem(i, order);
        }
    }

    private void applyAndAnimateMovedItems(List<Order> newOrders) {
        for (int toPosition = newOrders.size() - 1; toPosition >= 0; toPosition--) {
            final Order order = newOrders.get(toPosition);
            final int fromPosition = mOrders.indexOf(order);

            if (fromPosition >= 0 && fromPosition != toPosition)
                moveItem(fromPosition, toPosition);
        }
    }

    public void setOrdersListener(OrdersListener listener) {
        this.listener = listener;
    }

    public interface OrdersListener {
        void onOrderSelected(Order order);
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {

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
                    listener.onOrderSelected(mOrders.get(getLayoutPosition()));
            });
        }

        public void bind(Order order) {
            orderIDLabel.setText(order.getFactureID());
            statusColorLayout.setBackgroundColor(
                    Color.parseColor(order.getFacture() == 1 ? "#2E7D32" : "#D50000"));
        }
    }
}