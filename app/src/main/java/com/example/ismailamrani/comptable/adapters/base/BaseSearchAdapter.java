package com.example.ismailamrani.comptable.adapters.base;

import android.content.Context;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 27/05/2016.
 *
 * This adapter provides search functionality for the concerned RecyclerView.
 */
public abstract class BaseSearchAdapter<T extends BinderViewHolder<U>, U>
        extends BinderAdapter<U, T> {

    public BaseSearchAdapter(Context context, List<U> items) {
        super(context, items);
    }

    public U removeItem(int position) {
        final U item = mItems.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, U item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final U item = mItems.remove(fromPosition);
        mItems.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<U> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<U> newItems) {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            final U item = mItems.get(i);
            if (!newItems.contains(item))
                removeItem(i);
        }
    }

    private void applyAndAnimateAdditions(List<U> newItems) {
        for (int i = 0, count = newItems.size(); i < count; i++) {
            final U item = newItems.get(i);
            if (!mItems.contains(item))
                addItem(i, item);
        }
    }

    private void applyAndAnimateMovedItems(List<U> newItems) {
        for (int toPosition = newItems.size() - 1; toPosition >= 0; toPosition--) {
            final U item = newItems.get(toPosition);
            final int fromPosition = mItems.indexOf(item);

            if (fromPosition >= 0 && fromPosition != toPosition)
                moveItem(fromPosition, toPosition);
        }
    }
}
