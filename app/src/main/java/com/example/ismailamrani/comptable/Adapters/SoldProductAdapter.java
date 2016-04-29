package com.example.ismailamrani.comptable.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 27/04/2016.
 */
public class SoldProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> products;

    public SoldProductAdapter(Context context, List<Product> products) {
        super(context, -1, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.sold_product_row, parent, false);
            // Setup the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.productText = (TextView) convertView.findViewById(R.id.productText);
            // store the holder with the view.
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product product = products.get(position);
        System.out.println("Product is null: " + (product == null));

        viewHolder.productText.setText(product.getLibelle());
        return convertView;
    }

    static class ViewHolder {
        TextView productText;
    }
}