package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.ProductDetailsActivity;
import com.example.ismailamrani.comptable.utils.ActivityTransition;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 30/04/2016.
 */
public class StockAdapter extends BaseSearchAdapter<StockAdapter.ViewHolder, Product> {

    public StockAdapter(Context context, List<Product> products) {
        super(context, products);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_stock_row, viewGroup, false);

        return new ViewHolder(v);
    }

    public class ViewHolder extends BinderViewHolder<Product> {
        TextView productLabel;
        TextView quantityLabel;
        TextView colorFilterLayout;
        ImageView productImage;

        ViewHolder(View v) {
            super(v);
            productLabel = (TextView) v.findViewById(R.id.productLabel);
            quantityLabel = (TextView) v.findViewById(R.id.quantityLabel);
            colorFilterLayout = (TextView) v.findViewById(R.id.colorFilterLayout);
            productImage = (ImageView) v.findViewById(R.id.productImage);

            v.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("product", mItems.get(getLayoutPosition()));

                ActivityTransition.startActivityWithMultipleSharedElements(
                        mContext, intent,
                        Pair.create(productLabel, "productLabelAnim"),
                        Pair.create(productImage, "productImageAnim")
                );
            });
        }

        @Override
        public void bind(Product product) {
            quantityLabel.setTextColor(
                    mContext.getResources().getColor(product.getQte() == 0 ?
                            R.color.colorAccent : R.color.colorWhite));

            productLabel.setText(product.getLibelle());
            quantityLabel.setText(String.valueOf(product.getQte()));
            Picasso.with(itemView.getContext())
                    .load(PhpAPI.IpBackend_IMAGES + product.getPhoto())
                    .into(productImage);
        }
    }
}