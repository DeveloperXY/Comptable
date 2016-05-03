package com.example.ismailamrani.comptable.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private SaleProductListener listener;
    private List<Product> products;
    private int lastPosition;

    public ProductAdapter(Context context, List<Product> products) {
        super(context, -1, products);
        this.context = context;
        this.products = products;

        lastPosition = -1;
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
            viewHolder.deleteIcon = (ImageView) convertView.findViewById(R.id.deleteIcon);
            // store the holder with the view.
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.deleteIcon
                .setOnClickListener(v -> {
                    lastPosition = -1;

                    products.remove(position);
                    notifyDataSetChanged();
                    listener.onProductRemoved();
                });

        Product product = products.get(position);
        viewHolder.productText
                .setText(String.format("%s x %d", product.getLibelle(), product.getQte()));

        convertView.setOnTouchListener((v, event) -> {
            if (viewHolder.deleteIcon.getVisibility() == View.INVISIBLE) {
                viewHolder.deleteIcon.setVisibility(View.VISIBLE);
                if (lastPosition != -1) {
                    ((ViewHolder) parent.getChildAt(lastPosition)
                            .getTag())
                            .deleteIcon.setVisibility(View.INVISIBLE);
                }

                lastPosition = position;
            }
            else {
                viewHolder.deleteIcon.setVisibility(View.INVISIBLE);
                lastPosition = -1;
            }

            return false;
        });

        return convertView;
    }

    /**
     * @return a JSON array that represents the summary of the current
     * sale order.
     */
    public JSONArray getSummary() {
        JSONArray data = new JSONArray();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            JSONObject obj = new JSONObject();

            try {
                obj.put("productID", product.getID());
                obj.put("quantity", product.getQte());
                obj.put("priceHT", product.getPrixHT());
                obj.put("priceTTC", product.getPrixTTC());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            data.put(obj);
        }

        return data;
    }

    static class ViewHolder {
        TextView productText;
        ImageView deleteIcon;
    }

    public void setListener(SaleProductListener listener) {
        this.listener = listener;
    }

    public interface SaleProductListener {
        void onProductRemoved();
    }
}