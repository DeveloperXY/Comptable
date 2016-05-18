package com.example.ismailamrani.comptable.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.SearchAdapter;
import com.example.ismailamrani.comptable.ui.AddFournisseurActivity;
import com.example.ismailamrani.comptable.ui.AddProductActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class PurchaseChooserDialog extends ChooserDialog implements SearchView.OnQueryTextListener {

    private int sourceSpinnerID;

    @Bind(R.id.actionButton)
    protected Button actionButton;

    public PurchaseChooserDialog(Context context, int sourceSpinnerID) {
        super(context);
        this.sourceSpinnerID = sourceSpinnerID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionButton.setOnClickListener(v -> onActionButtonClicked());
    }

    @Override
    protected void onActionButtonClicked() {
        if (items.size() == 0) {
                /* The dialog's RecyclerView has no items.
                Decide the destination activity to be launched based on the
                ID of the spinner who started the dialog */
            Class<?> targetActivity = sourceSpinnerID == R.id.productSpinner ?
                    AddProductActivity.class : AddFournisseurActivity.class;
            context.startActivity(new Intent(context, targetActivity));
        }

        super.onActionButtonClicked();
    }

    @Override
    protected void whenNoItemsArePresent() {
        super.whenNoItemsArePresent();
        actionButton.setText(sourceSpinnerID == R.id.productSpinner ?
                "Add a new product" : "Add a new supplier");
    }
}
