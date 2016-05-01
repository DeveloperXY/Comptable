package com.example.ismailamrani.comptable.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Product;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 27/04/2016.
 * <p>
 * The prices of the products that are handled by this adapter do not reflect
 * their real prices in the distant database: they are calculated based on the
 * following formula:
 * <p>
 * price = original_price * quantity
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

        viewHolder.productText
                .setText(String.format("%s x %d", product.getLibelle(), product.getQte()));
        return convertView;
    }

    static class ViewHolder {
        TextView productText;
    }
}