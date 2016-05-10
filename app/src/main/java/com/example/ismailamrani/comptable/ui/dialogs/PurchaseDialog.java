package com.example.ismailamrani.comptable.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.example.ismailamrani.comptable.R;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class PurchaseDialog extends Dialog {
    public PurchaseDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.purchase_dialog);
    }
}
