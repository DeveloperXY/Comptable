package com.example.ismailamrani.comptable.adapters.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 29/05/2016.
 *
 * This adapter provides basic binding for the concerned RecyclerView.
 *
 * U : the type of data the adapter will hold
 * T: A view holder class that extends BinderViewHolder that does the binding.
 */
public abstract class BinderAdapter<U, T extends BinderViewHolder<U>> extends RecyclerView.Adapter<T> {

    protected Context mContext;
    protected List<U> mItems;

    public BinderAdapter(Context context, List<U> items) {
        mContext = context;
        mItems = new ArrayList<>(items);
    }

    @Override
    public void onBindViewHolder(T viewHolder, int position) {
        U item = mItems.get(position);
        viewHolder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
