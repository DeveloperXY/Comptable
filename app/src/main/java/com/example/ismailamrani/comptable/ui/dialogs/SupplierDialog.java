package com.example.ismailamrani.comptable.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.utils.ui.CalculateScreenSize;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class SupplierDialog extends Dialog {

    @Bind(R.id.supplierTitleLabel)
    TextView supplierTitleLabel;
    @Bind(R.id.supplierAddressLabel)
    TextView supplierAddressLabel;
    @Bind(R.id.bottomAddressLabel)
    TextView bottomAddressLabel;
    @Bind(R.id.supplierPhoneLabel)
    TextView supplierPhoneLabel;

    @Bind(R.id.closeDialogItem)
    ImageView closeDialogItem;
    @Bind(R.id.editSupplierItem)
    ImageView editSupplierItem;
    @Bind(R.id.deleteSupplierItem)
    ImageView deleteSupplierItem;

    private Context mContext;
    private Supplier supplier;
    private SupplierDialogListener mDialogListener;

    public SupplierDialog(Context context, Supplier supplier, SupplierDialogListener listener) {
        super(context);
        mContext = context;
        this.supplier = supplier;
        mDialogListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.supplier_dialog);
        ButterKnife.bind(this);

        setupWindow();
        setupListeners();
        initializeUI();
    }

    private void setupWindow() {
        Point point = new Point();
        new CalculateScreenSize().calculateScreenSize(mContext, point);
        getWindow().setLayout((int) (point.x - point.x * 0.1), (int) (point.y - point.y * 0.3));
    }

    private void setupListeners() {
        closeDialogItem.setOnClickListener(v -> dismiss());
        editSupplierItem.setOnClickListener(v -> {
            dismiss();
            if (mDialogListener != null)
                mDialogListener.onEdit(supplier.getId());
        });
        deleteSupplierItem.setOnClickListener(v -> {
            if (mDialogListener != null)
                mDialogListener.onDelete(supplier.getId());
        });
    }

    private void initializeUI() {
        String address = supplier.getAdresse();

        supplierTitleLabel.setText(supplier.getNom());
        supplierAddressLabel.setText(address);
        bottomAddressLabel.setText(address);
        supplierPhoneLabel.setText(supplier.getTel());
    }

    public interface SupplierDialogListener {
        void onDelete(String supplierID);
        void onEdit(String supplierID);
    }
}
