package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Charge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 21/05/2016.
 */
public class ChargeAdapter extends RecyclerView.Adapter<ChargeAdapter.ViewHolder> {

    private Context mContext;
    private List<Charge> mCharges;
    private ChargeAdapterListener mChargeAdapterListener;

    public ChargeAdapter(Context context, List<Charge> charges) {
        mContext = context;
        mCharges = new ArrayList<>(charges);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.charge_item_row, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Charge charge = mCharges.get(position);
        viewHolder.bind(charge);
    }

    @Override
    public int getItemCount() {
        return mCharges.size();
    }

    public Charge removeItem(int position) {
        final Charge charge = mCharges.remove(position);
        notifyItemRemoved(position);
        return charge;
    }

    public void addItem(int position, Charge charge) {
        mCharges.add(position, charge);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Charge charge = mCharges.remove(fromPosition);
        mCharges.add(toPosition, charge);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Charge> charges) {
        Log.i("LIST", "OLD: " + mCharges);
        Log.i("LIST", "NEW: " + charges);
        applyAndAnimateRemovals(charges);
        applyAndAnimateAdditions(charges);
        applyAndAnimateMovedItems(charges);
    }

    private void applyAndAnimateRemovals(List<Charge> newCharges) {
        for (int i = mCharges.size() - 1; i >= 0; i--) {
            final Charge charge = mCharges.get(i);
            if (!newCharges.contains(charge))
                removeItem(i);
        }
    }

    private void applyAndAnimateAdditions(List<Charge> newCharges) {
        for (int i = 0, count = newCharges.size(); i < count; i++) {
            final Charge charge = newCharges.get(i);
            if (!mCharges.contains(charge))
                addItem(i, charge);
        }
    }

    private void applyAndAnimateMovedItems(List<Charge> newCharges) {
        for (int toPosition = newCharges.size() - 1; toPosition >= 0; toPosition--) {
            final Charge charge = newCharges.get(toPosition);
            final int fromPosition = mCharges.indexOf(charge);

            if (fromPosition >= 0 && fromPosition != toPosition)
                moveItem(fromPosition, toPosition);
        }
    }

    public double getTotalPrice() {
        double total = 0;

        for (int i = 0; i < mCharges.size(); i++)
            total += mCharges.get(i).getPrice();

        return total;
    }

    public void setListener(ChargeAdapterListener listener) {
        mChargeAdapterListener = listener;
    }

    public interface ChargeAdapterListener {
        long getServerTimeInMillis();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView descriptionLabel;
        TextView timeLabel;
        TextView chargePriceLabel;

        ViewHolder(View v) {
            super(v);
            descriptionLabel = (TextView) v.findViewById(R.id.descriptionLabel);
            timeLabel = (TextView) v.findViewById(R.id.timeLabel);
            chargePriceLabel = (TextView) v.findViewById(R.id.chargePriceLabel);
        }

        public void bind(Charge charge) {
            descriptionLabel.setText(charge.getDescription());
            chargePriceLabel.setText(charge.getPrice() + " DH");
            timeLabel.setText(charge.getDateFrom());
        }
    }
}