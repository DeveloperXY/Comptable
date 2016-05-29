package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Supplier;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Redouane on 08/04/2016.
 */
public class SupplierAdapter extends BaseSearchAdapter<SupplierAdapter.ViewHolder, Supplier> {

    private SupplierListener listener;

    public SupplierAdapter(Context context, List<Supplier> suppliers) {
        super(context, suppliers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.supplier_grid_item, viewGroup, false);

        return new ViewHolder(v);
    }

    public class ViewHolder extends BinderViewHolder<Supplier> {
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
                    listener.onSupplierSelected(mItems.get(getAdapterPosition()));
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
                    new MyMenuItemClickListener(mItems.get(getAdapterPosition())));
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
                        if (listener != null)
                            listener.onEditSupplier(supplier.getId());
                        return true;
                    case R.id.action_delete_supplier:
                        if (listener != null)
                            listener.onDeleteSupplier(supplier.getId());
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
        void onEditSupplier(String supplierID);
        void onDeleteSupplier(String supplierID);
    }
}
