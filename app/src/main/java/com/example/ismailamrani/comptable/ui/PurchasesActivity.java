package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.customitems.dialogs.SpinnerBottomSheet;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.models.Supplier;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchasesActivity extends ColoredStatusBarActivity
        implements OGActionBarInterface {

    private Product selectedProduct;
    private Supplier selectedSupplier;

    private OGActionBar mActionBar;
    private SpinnerBottomSheet bottomSheetDialog;

    @Bind(R.id.productField)
    EditText productField;
    @Bind(R.id.supplierField)
    EditText supplierField;
    @Bind(R.id.nextButton)
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);
        ButterKnife.bind(this);

        nextButton.requestFocus();

        setupActionBar();
    }

    private void setupActionBar() {
        mActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Achats");
        mActionBar.disableAddButton();
    }

    @OnClick({R.id.supplierSpinner, R.id.productSpinner})
    public void onSpinnerClick(View view) {
        showBottomSheetDialog(view.getId());
    }

    private void showBottomSheetDialog(int spinnerID) {
        bottomSheetDialog = new SpinnerBottomSheet(this, spinnerID);
        bottomSheetDialog.setListener(item -> {
            if (item instanceof Product) {
                // A product has been selected
                selectedProduct = ((Product) item);
                productField.setText(selectedProduct.getLibelle());
            }
            else {
                // A supplier has been selected
                selectedSupplier = ((Supplier) item);
                supplierField.setText(selectedSupplier.getNom());
            }
        });
        bottomSheetDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(bottomSheetDialog != null)
            bottomSheetDialog.dismiss();
    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }
}
