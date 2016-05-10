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
public class PurchaseChooserDialog extends Dialog implements SearchView.OnQueryTextListener {

    private Context context;
    private SearchAdapter searchAdapter;
    private OnItemSelectionListener itemSelectionListener;

    @Bind(R.id.dialogSearchView)
    SearchView dialogSearchView;
    @Bind(R.id.dialogRecyclerView)
    RecyclerView dialogRecyclerView;
    @Bind(R.id.actionButton)
    Button actionButton;
    @Bind(R.id.emptyView)
    TextView emptyView;

    private List<String> items;
    private String hint;
    private int sourceSpinnerID;

    public PurchaseChooserDialog(Context context, int sourceSpinnerID) {
        super(context);
        this.context = context;
        this.sourceSpinnerID = sourceSpinnerID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.purchase_dialog);
        ButterKnife.bind(this);

        actionButton.setOnClickListener(v -> {
            if (items.size() == 0) {
                /* The dialog's RecyclerView has no items.
                Decide the destination activity to be launched based on the
                ID of the spinner who started the dialog */
                Class<?> targetActivity = sourceSpinnerID == R.id.productSpinner ?
                        AddProductActivity.class : AddFournisseurActivity.class;
                context.startActivity(new Intent(context, targetActivity));
            }
            else {
                // The dialog does have some items
                dismiss();
            }
        });
        if (!TextUtils.isEmpty(hint))
            setSearchHint(hint);

        setupSearchView();
        setupRecyclerView();
    }

    public PurchaseChooserDialog whoseItemsAre(List<String> items) {
        this.items = new ArrayList<>(items);
        return this;
    }

    public PurchaseChooserDialog whoseSearchHintIs(String hint) {
        this.hint = hint;
        return this;
    }

    public PurchaseChooserDialog runWhenItemSelected(OnItemSelectionListener listener) {
        itemSelectionListener = listener;
        return this;
    }

    private void setupSearchView() {
        dialogSearchView.setIconifiedByDefault(false);
        dialogSearchView.setOnQueryTextListener(this);
    }

    private void setupRecyclerView() {
        if (items == null)
            items = new ArrayList<>();

        if (items.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            dialogSearchView.setVisibility(View.GONE);
            actionButton.setText(sourceSpinnerID == R.id.productSpinner ?
                    "Add a new product" : "Add a new supplier");
        }

        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        searchAdapter = new SearchAdapter(context, items);
        searchAdapter.setListener(position -> {
            if (itemSelectionListener != null) {
                itemSelectionListener.onItemSelected(position);
                dismiss();
            }
        });
        dialogRecyclerView.setAdapter(searchAdapter);
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
        void onItemSelected(int position);
    }
}
