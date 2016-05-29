package com.example.ismailamrani.comptable.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Mohammed Aouf ZOUAG on 29/05/2016.
 */
public abstract class BinderViewHolder<T> extends RecyclerView.ViewHolder {
    public BinderViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T item);
}
