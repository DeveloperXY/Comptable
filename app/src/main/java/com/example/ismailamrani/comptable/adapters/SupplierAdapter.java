package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.ui.EditFournisseurActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Redouane on 08/04/2016.
 */
public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {

    private Context mContext;
    private List<Supplier> mSuppliers;
    private SupplierListener listener;

    public SupplierAdapter(Context context, List<Supplier> suppliers) {
        mContext = context;
        mSuppliers = new ArrayList<>(suppliers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.supplier_grid_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Supplier supplier = mSuppliers.get(position);
        viewHolder.bind(supplier);
    }

    @Override
    public int getItemCount() {
        return mSuppliers.size();
    }

    public Supplier removeItem(int position) {
        final Supplier supplier = mSuppliers.remove(position);
        notifyItemRemoved(position);
        return supplier;
    }

    public void addItem(int position, Supplier supplier) {
        mSuppliers.add(position, supplier);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Supplier supplier = mSuppliers.remove(fromPosition);
        mSuppliers.add(toPosition, supplier);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Supplier> suppliers) {
        applyAndAnimateRemovals(suppliers);
        applyAndAnimateAdditions(suppliers);
        applyAndAnimateMovedItems(suppliers);
    }

    private void applyAndAnimateRemovals(List<Supplier> newSuppliers) {
        for (int i = mSuppliers.size() - 1; i >= 0; i--) {
            final Supplier supplier = mSuppliers.get(i);
            if (!newSuppliers.contains(supplier))
                removeItem(i);
        }
    }

    private void applyAndAnimateAdditions(List<Supplier> newSuppliers) {
        for (int i = 0, count = newSuppliers.size(); i < count; i++) {
            final Supplier supplier = newSuppliers.get(i);
            if (!mSuppliers.contains(supplier))
                addItem(i, supplier);
        }
    }

    private void applyAndAnimateMovedItems(List<Supplier> newSuppliers) {
        for (int toPosition = newSuppliers.size() - 1; toPosition >= 0; toPosition--) {
            final Supplier supplier = newSuppliers.get(toPosition);
            final int fromPosition = mSuppliers.indexOf(supplier);

            if (fromPosition >= 0 && fromPosition != toPosition)
                moveItem(fromPosition, toPosition);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.thumbnail)
        ImageView thumbnail;
        @Bind(R.id.overflow)
        ImageView overflow;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            View.OnClickListener clickListener = view -> {
                if (listener != null)
                    listener.onSupplierSelected(mSuppliers.get(getAdapterPosition()));
            };

            overflow.setOnClickListener(this::showPopupMenu);
            v.setOnClickListener(clickListener);
            thumbnail.setOnClickListener(clickListener);
        }

        public void bind(Supplier supplier) {
            title.setText(supplier.getNom());
            address.setText(supplier.getAdresse());
            thumbnail.setImageResource(R.drawable.flogo);
        }

        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(mContext, view);
            popup.inflate(R.menu.menu_supplier_item);
            popup.setOnMenuItemClickListener(
                    new MyMenuItemClickListener(mSuppliers.get(getAdapterPosition())));
            popup.show();
        }

        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            private Supplier supplier;

            public MyMenuItemClickListener(Supplier supplier) {
                this.supplier = supplier;
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_edit_supplier:
                        Intent i = new Intent(mContext, EditFournisseurActivity.class);
                        i.putExtra("id", supplier.getId());
                        mContext.startActivity(i);
                        return true;
                    case R.id.action_delete_supplier:
                        Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                }
                return false;
            }
        }
    }

    public void setSupplierListener(SupplierListener listener) {
        this.listener = listener;
    }

    public interface SupplierListener {
        void onSupplierSelected(Supplier supplier);
    }
}
