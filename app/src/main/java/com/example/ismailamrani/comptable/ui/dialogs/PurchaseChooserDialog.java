package com.example.ismailamrani.comptable.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SearchView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.SearchAdapter;
import com.example.ismailamrani.comptable.ui.AlterSupplierActivity;
import com.example.ismailamrani.comptable.ui.AddProductActivity;
import com.example.ismailamrani.comptable.ui.dialogs.base.ChooserDialog;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class PurchaseChooserDialog extends ChooserDialog<String>
        implements SearchView.OnQueryTextListener {

    private int sourceSpinnerID;

    public PurchaseChooserDialog(Context context, int sourceSpinnerID) {
        super(context, new SearchAdapter.StringViewHolderBinder());
        this.sourceSpinnerID = sourceSpinnerID;
    }

    @Override
    protected void onActionButtonClicked() {
        if (mItems.size() == 0) {
            /* The dialog's RecyclerView has no items.
            Decide the destination activity to be launched based on the
            ID of the spinner who started the dialog */
            Class<?> targetActivity = sourceSpinnerID == R.id.productSpinner ?
                    AddProductActivity.class : AlterSupplierActivity.class;
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

    @Override
    public boolean onQueryTextChange(String query) {
        final List<String> filteredItems = filter(mItems, query);
        searchAdapter.animateTo(filteredItems);
        return true;
    }

    public List<String> filter(List<String> items, String query) {
        return Stream.of(items)
                .filter(item -> item.toLowerCase()
                        .contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
