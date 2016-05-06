package com.example.ismailamrani.comptable.ui.stock;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.ProductDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
        final Product product = mProducts.remove(position);
        notifyItemRemoved(position);
        return product;
    }

    public void addItem(int position, Product product) {
        mProducts.add(position, product);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Product product = mProducts.remove(fromPosition);
        mProducts.add(toPosition, product);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Product> products) {
        applyAndAnimateRemovals(products);
        applyAndAnimateAdditions(products);
        applyAndAnimateMovedItems(products);
    }

    private void applyAndAnimateRemovals(List<Product> newProducts) {
        for (int i = mProducts.size() - 1; i >= 0; i--) {
            final Product product = mProducts.get(i);
            if (!newProducts.contains(product))
                removeItem(i);
        }
    }

    private void applyAndAnimateAdditions(List<Product> newProducts) {
        for (int i = 0, count = newProducts.size(); i < count; i++) {
            final Product product = newProducts.get(i);
            if (!mProducts.contains(product))
                addItem(i, product);
        }
    }

    private void applyAndAnimateMovedItems(List<Product> newProducts) {
        for (int toPosition = newProducts.size() - 1; toPosition >= 0; toPosition--) {
            final Product product = newProducts.get(toPosition);
            final int fromPosition = mProducts.indexOf(product);

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

            v.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("id", mProducts.get(getLayoutPosition()).getID());
                mContext.startActivity(intent);
            });
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