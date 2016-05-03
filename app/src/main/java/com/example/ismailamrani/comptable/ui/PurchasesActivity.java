package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.customitems.dialogs.SpinnerBottomSheet;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchasesActivity extends AppCompatActivity
        implements OGActionBarInterface {

    private OGActionBar mActionBar;
    private SpinnerBottomSheet bottomSheetDialog;

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
        showBottomSheetDialog(view.getId());
    }

    private void showBottomSheetDialog(int spinnerID) {
        bottomSheetDialog = new SpinnerBottomSheet(this, spinnerID);
        bottomSheetDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        bottomSheetDialog.dismiss();
    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }
}
