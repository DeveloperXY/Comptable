package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Charge;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 21/05/2016.
 */
public class ChargeAdapter extends BaseSearchAdapter<ChargeAdapter.ViewHolder, Charge> {

    public ChargeAdapter(Context context, List<Charge> charges) {
        super(context, charges);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.charge_item_row, viewGroup, false);

        return new ViewHolder(v);
    }

    public double getTotalPrice() {
        double total = 0;

        for (int i = 0; i < mItems.size(); i++)
            total += mItems.get(i).getPrice();

        return total;
    }

    public class ViewHolder extends BinderViewHolder<Charge> {

        TextView descriptionLabel;
        TextView timeLabel;
        TextView chargePriceLabel;

        ViewHolder(View v) {
            super(v);
            descriptionLabel = (TextView) v.findViewById(R.id.descriptionLabel);
            timeLabel = (TextView) v.findViewById(R.id.timeLabel);
            chargePriceLabel = (TextView) v.findViewById(R.id.chargePriceLabel);
        }

        @Override
        public void bind(Charge charge) {
            descriptionLabel.setText(charge.getDescription());
            chargePriceLabel.setText(charge.getPrice() + " DH");
            timeLabel.setText(charge.getDateFrom());
        }
    }
}