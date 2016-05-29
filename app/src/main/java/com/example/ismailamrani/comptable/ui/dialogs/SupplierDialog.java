package com.example.ismailamrani.comptable.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.utils.CalculateScreenSize;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class SupplierDialog extends Dialog {

    private Context mContext;
    private Supplier supplier;

    public SupplierDialog(Context context, Supplier supplier) {
        super(context);
        mContext = context;
        supplier = supplier;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.supplier_dialog);

        setupWindow();
        fetchSupplierDetails();
    }

    private void setupWindow() {
        Point point = new Point();
        new CalculateScreenSize().calculateScreenSize(mContext, point);
        getWindow().setLayout((int) (point.x - point.x * 0.1), (int) (point.y - point.y * 0.3));
    }

    private void fetchSupplierDetails() {

    }
}