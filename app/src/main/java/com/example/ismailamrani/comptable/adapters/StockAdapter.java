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
                    .transform(new BlurTransformation(itemView.getContext()))
                    .into(productImage);
        }
    }
}