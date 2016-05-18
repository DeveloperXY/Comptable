package com.example.ismailamrani.comptable.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.Button;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.AddFournisseurActivity;
import com.example.ismailamrani.comptable.ui.AddProductActivity;

import butterknife.Bind;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class LocalChooserDialog extends ChooserDialog implements SearchView.OnQueryTextListener {

    public LocalChooserDialog(Context context) {
        super(context);
    }

    @Override
    protected void whenNoItemsArePresent() {
        super.whenNoItemsArePresent();
        actionButton.setText("Close");
    }
}
