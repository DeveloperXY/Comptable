package com.example.ismailamrani.comptable.ui.dialogs;

import android.content.Context;
import android.support.v7.widget.SearchView;

import com.example.ismailamrani.comptable.adapters.SearchAdapter;
import com.example.ismailamrani.comptable.models.Local;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class LocalChooserDialog extends ChooserDialog<Local>
        implements SearchView.OnQueryTextListener {

    public LocalChooserDialog(Context context) {
        super(context, new SearchAdapter.LocaleViewHolderBinder());
    }

    @Override
    protected void whenNoItemsArePresent() {
        super.whenNoItemsArePresent();
        actionButton.setText("Close");
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Local> filteredLocals = Local.filter(mItems, query);
        searchAdapter.animateTo(filteredLocals);
        return true;
    }
}
