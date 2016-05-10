package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 30/04/2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mItems;

    public SearchAdapter(Context context, List<String> items) {
        mContext = context;
        mItems = new ArrayList<>(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_row, viewGroup, false);

        return new ViewHolder(mContext, v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = mItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public String removeItem(int position) {
        final String item = mItems.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, String item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final String item = mItems.remove(fromPosition);
        mItems.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<String> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<String> newItems) {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            final String item = mItems.get(i);
            if (!newItems.contains(item))
                removeItem(i);
        }
    }

    private void applyAndAnimateAdditions(List<String> newItems) {
        for (int i = 0, count = newItems.size(); i < count; i++) {
            final String item = newItems.get(i);
            if (!mItems.contains(item))
                addItem(i, item);
        }
    }

    private void applyAndAnimateMovedItems(List<String> newItems) {
        for (int toPosition = newItems.size() - 1; toPosition >= 0; toPosition--) {
            final String item = newItems.get(toPosition);
            final int fromPosition = mItems.indexOf(item);

            if (fromPosition >= 0 && fromPosition != toPosition)
                moveItem(fromPosition, toPosition);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textLabel;

        public ViewHolder(Context context, View v) {
            super(v);
            textLabel = (TextView) v.findViewById(R.id.orderIDLabel);
        }

        public void bind(String item) {

        }
    }
}