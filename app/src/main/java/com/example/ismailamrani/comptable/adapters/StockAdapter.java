package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * Created by Mohammed Aouf ZOUAG on 30/04/2016.
 */
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    private Context mContext;
    private List<Product> mProducts;

    public StockAdapter(Context context, List<Product> products) {
        mContext = context;
        mProducts = new ArrayList<>(products);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_stock_row, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Product product = mProducts.get(position);
        viewHolder.bind(product);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public Product removeItem(int position) {
        final Product contact = mProducts.remove(position);
        notifyItemRemoved(position);
        return contact;
    }

    public void addItem(int position, Product contact) {
        mProducts.add(position, contact);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Product contact = mProducts.remove(fromPosition);
        mProducts.add(toPosition, contact);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Product> contacts) {
        applyAndAnimateRemovals(contacts);
        applyAndAnimateAdditions(contacts);
        applyAndAnimateMovedItems(contacts);
    }

    private void applyAndAnimateRemovals(List<Product> newContacts) {
        for (int i = mProducts.size() - 1; i >= 0; i--) {
            final Product contact = mProducts.get(i);
            if (!newContacts.contains(contact))
                removeItem(i);
        }
    }

    private void applyAndAnimateAdditions(List<Product> newContacts) {
        for (int i = 0, count = newContacts.size(); i < count; i++) {
            final Product contact = newContacts.get(i);
            if (!mProducts.contains(contact))
                addItem(i, contact);
        }
    }

    private void applyAndAnimateMovedItems(List<Product> newContacts) {
        for (int toPosition = newContacts.size() - 1; toPosition >= 0; toPosition--) {
            final Product contact = newContacts.get(toPosition);
            final int fromPosition = mProducts.indexOf(contact);

            if (fromPosition >= 0 && fromPosition != toPosition)
                moveItem(fromPosition, toPosition);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productLabel;
        TextView quantityLabel;
        ImageView productImage;

        ViewHolder(View v) {
            super(v);
            productLabel = (TextView) v.findViewById(R.id.productLabel);
            quantityLabel = (TextView) v.findViewById(R.id.quantityLabel);
            productImage = (ImageView) v.findViewById(R.id.productImage);
        }

        public void bind(Product product) {
            productLabel.setText(product.getLibelle());
            quantityLabel.setText(String.valueOf(product.getQte()));
            Picasso.with(itemView.getContext())
                    .load(R.drawable.iphone)
                    .into(productImage);
        }
    }
}