package com.example.ismailamrani.comptable.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.example.ismailamrani.comptable.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class PurchaseChooserDialog extends Dialog {

    private List<String> items;

    public PurchaseChooserDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.purchase_dialog);
    }

    public PurchaseChooserDialog whoseItemsAre(List<String> items) {
        this.items = new ArrayList<>(items);
        return this;
    }

    public PurchaseChooserDialog whoseItemsAre(String[] items) {
        this.items = Arrays.asList(items);
        return this;
    }
}
