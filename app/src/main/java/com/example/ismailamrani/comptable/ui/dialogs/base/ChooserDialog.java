package com.example.ismailamrani.comptable.ui.dialogs.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.ismailamrani.comptable.utils.ui.WindowUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public abstract class ChooserDialog<T> extends Dialog implements SearchView.OnQueryTextListener {

    protected Context context;
    protected SearchAdapter<T> searchAdapter;
    private OnItemSelectionListener<T> itemSelectionListener;

    @Bind(R.id.dialogSearchView)
    protected SearchView dialogSearchView;
    @Bind(R.id.dialogRecyclerView)
    protected RecyclerView dialogRecyclerView;
    @Bind(R.id.emptyView)
    protected TextView emptyView;
    @Bind(R.id.actionButton)
    protected Button actionButton;

    protected List<T> mItems;
    protected String hint;
    protected SearchAdapter.ViewHolderBinder<T> binder;

    public ChooserDialog(Context context, SearchAdapter.ViewHolderBinder<T> binder) {
        super(context);
        this.context = context;
        this.binder = binder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.purchase_dialog);
        ButterKnife.bind(this);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionButtonClicked();
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                WindowUtils.hideKeyboard((Activity) context);
            }
        });

        if (!TextUtils.isEmpty(hint))
            setSearchHint(hint);

        setupSearchView();
        setupRecyclerView();
    }

    public ChooserDialog whoseItemsAre(List<T> items) {
        this.mItems = new ArrayList<>(items);
        return this;
    }

    public ChooserDialog whoseSearchHintIs(String hint) {
        this.hint = hint;
        return this;
    }

    public ChooserDialog runWhenItemSelected(OnItemSelectionListener<T> listener) {
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
        if (mItems == null)
            mItems = new ArrayList<>();

        if (mItems.size() == 0) {
            whenNoItemsArePresent();
        }

        dialogRecyclerView.setHasFixedSize(true);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        searchAdapter = new SearchAdapter<>(context, mItems, binder);
        searchAdapter.setListener(new SearchAdapter.OnItemClickListener<T>() {
            @Override
            public void onItemClicked(T item) {
                if (itemSelectionListener != null) {
                    itemSelectionListener.onItemSelected(item);
                    dismiss();
                }
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

    public interface OnItemSelectionListener<T> {
        void onItemSelected(T item);
    }
}
