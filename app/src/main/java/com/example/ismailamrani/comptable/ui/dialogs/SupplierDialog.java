package com.example.ismailamrani.comptable.ui.dialogs;

import android.content.Context;

import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.ui.dialogs.base.ItemDialog;

/**
 * Created by Mohammed Aouf ZOUAG on 30/05/2016.
 */
public class SupplierDialog extends ItemDialog<Supplier> {

    public SupplierDialog(Context context, Supplier supplier) {
        super(context, supplier);
    }

    @Override
    protected void bind(Supplier item) {
        titleLabel.setText(item.getNom());
        emailLabel.setText(item.getEmail());
        addressLabel.setText(item.getAdresse());
        phoneLabel.setText(item.getTel());
    }
}
