package com.example.ismailamrani.comptable.ui.dialogs;

import android.app.Dialog;
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
public abstract class ChooserDialog extends Dialog implements SearchView.OnQueryTextListener {

    protected Context context;
    private SearchAdapter searchAdapter;
    private OnItemSelectionListener itemSelectionListener;

    @Bind(R.id.dialogSearchView)
    protected SearchView dialogSearchView;
    @Bind(R.id.dialogRecyclerView)
    protected RecyclerView dialogRecyclerView;
    @Bind(R.id.emptyView)
    protected TextView emptyView;

    protected List<String> items;
    protected String hint;

    public ChooserDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.purchase_dialog);
        ButterKnife.bind(this);

        if (!TextUtils.isEmpty(hint))
            setSearchHint(hint);

        setupSearchView();
        setupRecyclerView();
    }

    public ChooserDialog whoseItemsAre(List<String> items) {
        this.items = new ArrayList<>(items);
        return this;
    }

    public ChooserDialog whoseSearchHintIs(String hint) {
        this.hint = hint;
        return this;
    }

    public ChooserDialog runWhenItemSelected(OnItemSelectionListener listener) {
        itemSelectionListener = listener;
        return this;
    }

    protected void onActionButtonClicked() {
        dismiss();
    }

    private void setupSearchView() {
        dialogSearchView.setIconifiedByDefault(false);
        dialogSearchView.setOnQueryTextListener(this);
    }

    private void setupRecyclerView() {
        if (items == null)
            items = new ArrayList<>();

        if (items.size() == 0) {
            whenNoItemsArePresent();
        }

        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        searchAdapter = new SearchAdapter(context, items);
        searchAdapter.setListener(item -> {
            if (itemSelectionListener != null) {
                itemSelectionListener.onItemSelected(item);
                dismiss();
            }
        });
        dialogRecyclerView.setAdapter(searchAdapter);
    }

    /**
     * This method is invoked when the dialog has no items to show.
     */
    protected void whenNoItemsArePresent() {
        emptyView.setVisibility(View.VISIBLE);
        dialogSearchView.setVisibility(View.GONE);
    }

    /**
     * Specifies the query hint of the SearchView.
     *
     * @param hint text of the hint
     */
    private void setSearchHint(String hint) {
        dialogSearchView.setQueryHint(hint);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<String> filteredItems = searchAdapter.filter(items, query);
        searchAdapter.animateTo(filteredItems);
        return true;
    }

    public interface OnItemSelectionListener {
        void onItemSelected(String item);
    }
}
