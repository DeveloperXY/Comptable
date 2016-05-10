package com.example.ismailamrani.comptable.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Window;

import com.example.ismailamrani.comptable.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 10/05/2016.
 */
public class PurchaseChooserDialog extends Dialog {

    @Bind(R.id.dialogSearchView)
    SearchView dialogSearchView;

    private List<String> items;
    private String hint;

    public PurchaseChooserDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.purchase_dialog);
        ButterKnife.bind(this);

        if (!TextUtils.isEmpty(hint))
            setSearchHint(hint);
    }

    public PurchaseChooserDialog whoseItemsAre(List<String> items) {
        this.items = new ArrayList<>(items);
        return this;
    }

    public PurchaseChooserDialog whoseSearchHintIs(String hint) {
        this.hint = hint;
        return this;
    }

    /**
     * Specifies the query hint of the SearchView.
     * @param hint text of the hint
     */
    private void setSearchHint(String hint) {
        dialogSearchView.setQueryHint(hint);
    }
}
