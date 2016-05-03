package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.spinners.ItemAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.customitems.dialogs.SpinnerBottomSheet;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchasesActivity extends AppCompatActivity
        implements OGActionBarInterface {

    private OGActionBar mActionBar;
    BottomSheetBehavior behavior;
    private SpinnerBottomSheet bottomSheetDialog;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);
        ButterKnife.bind(this);

        setupActionBar();
    }

    private void setupActionBar() {
        mActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Achats");
        mActionBar.AddDisable();
    }

    @OnClick({R.id.supplierSpinner, R.id.productSpinner})
    public void onSpinnerClick(View view) {
        switch (view.getId()) {
            case R.id.supplierSpinner:
                showBottomSheetDialog();
                break;
            case R.id.productSpinner:

                break;
        }
    }

    private void showBottomSheetDialog() {
        bottomSheetDialog = new SpinnerBottomSheet(this);
        bottomSheetDialog.show();
    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }
}
