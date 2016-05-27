package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Local;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 30/04/2016.
 */
public class SearchAdapter<T> extends BaseSearchAdapter<SearchAdapter.ViewHolder<T>, T> {

    private Context mContext;
    private OnItemClickListener<T> listener;
    private ViewHolderBinder<T> mBinder;

    public SearchAdapter(Context context, List<T> items, ViewHolderBinder<T> binder) {
        mContext = context;
        mItems = new ArrayList<>(items);
        mBinder = binder;
    }

    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_item_row, viewGroup, false);

        return new ViewHolder<>(v, mBinder, listener, mItems);
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        T item = mItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface ViewHolderBinder<T> {
        void bind(ViewHolder<T> viewHolder, T item);
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        ViewHolderBinder<T> mBinder;
        TextView textLabel;

        public ViewHolder(View v, ViewHolderBinder<T> binder,
                          OnItemClickListener<T> listener, List<T> items) {
            super(v);
            mBinder = binder;
            textLabel = (TextView) v.findViewById(R.id.textLabel);

            v.setOnClickListener(view -> {
                if (listener != null)
                    listener.onItemClicked(items.get(getLayoutPosition()));
            });
        }

        public void bind(T item) {
            mBinder.bind(this, item);
        }
    }

    public static class StringViewHolderBinder implements ViewHolderBinder<String> {
        @Override
        public void bind(ViewHolder<String> viewHolder, String item) {
            viewHolder.textLabel.setText(item);
        }
    }

    public static class LocaleViewHolderBinder implements ViewHolderBinder<Local> {
        @Override
        public void bind(ViewHolder<Local> viewHolder, Local item) {
            viewHolder.textLabel.setText(item.getAddress());
        }
    }

    public void setListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClicked(T item);
    }
}