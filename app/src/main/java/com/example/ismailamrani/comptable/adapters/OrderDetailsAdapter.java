package com.example.ismailamrani.comptable.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.OrderDetail;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 13/05/2016.
 */
public class OrderDetailsAdapter extends ArrayAdapter<OrderDetail> {

    private Context context;
    private List<OrderDetail> detailsListItems;

    public OrderDetailsAdapter(Context context, List<OrderDetail> detailsListItems) {
        super(context, -1, detailsListItems);
        this.context = context;
        this.detailsListItems = detailsListItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.order_detail_row, parent, false);
            // Setup the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.productNameLabel = (TextView) convertView.findViewById(R.id.productNameLabel);
            viewHolder.quantityLabel = (TextView) convertView.findViewById(R.id.quantityLabel);
            viewHolder.priceLabel = (TextView) convertView.findViewById(R.id.priceLabel);
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.productImage);
            // store the holder with the view.
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OrderDetail detail = detailsListItems.get(position);
        viewHolder.bind(context, detail);

        return convertView;
    }

    static class ViewHolder {
        TextView productNameLabel;
        TextView quantityLabel;
        TextView priceLabel;
        ImageView productImage;

        public void bind(Context context, OrderDetail orderDetail) {
            productNameLabel.setText(orderDetail.getProductName());
            quantityLabel.setText(String.valueOf(orderDetail.getQuantity()));
            priceLabel.setText(String.format("%.0f DH", orderDetail.getPriceTTC()));
            Picasso.with(context)
                    .load(PhpAPI.IpBackend_IMAGES + orderDetail.getProductImage())
                    .into(productImage);
        }
    }
}