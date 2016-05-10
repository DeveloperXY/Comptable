package com.example.ismailamrani.comptable.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class PurchaseChooserDialog extends Dialog {

    private Context context;
    private SearchAdapter searchAdapter;

    @Bind(R.id.dialogSearchView)
    SearchView dialogSearchView;
    @Bind(R.id.dialogRecyclerView)
    RecyclerView dialogRecyclerView;
    @Bind(R.id.cancelButton)
    Button cancelButton;

    private List<String> items;
    private String hint;

    public PurchaseChooserDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.purchase_dialog);
        ButterKnife.bind(this);

        cancelButton.setOnClickListener(v -> dismiss());
        if (!TextUtils.isEmpty(hint))
            setSearchHint(hint);

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

    private void setupRecyclerView() {
        if (items == null)
            items = new ArrayList<>();
        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        searchAdapter = new SearchAdapter(context, items);
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
}
