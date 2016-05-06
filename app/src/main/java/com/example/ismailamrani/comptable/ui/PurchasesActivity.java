package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ProductOrderAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.customitems.dialogs.SpinnerBottomSheet;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PurchasesActivity extends ColoredStatusBarActivity
        implements OGActionBarInterface {

    private Product selectedProduct;
    private Supplier selectedSupplier;

    private OGActionBar mActionBar;
    private SpinnerBottomSheet bottomSheetDialog;
    private OkHttpClient client = new OkHttpClient();

    @Bind(R.id.productField)
    EditText productField;
    @Bind(R.id.supplierField)
    EditText supplierField;
    @Bind(R.id.quantityField)
    EditText quantityField;
    @Bind(R.id.priceField)
    EditText priceField;
    @Bind(R.id.productsListview)
    ListView productsListview;
    @Bind(R.id.priceLabel)
    TextView priceLabel;

    private Product mProduct;
    private List<Product> toBeBoughtProducts;

    private ProductOrderAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);
        ButterKnife.bind(this);

        setupActionBar();

        toBeBoughtProducts = new ArrayList<>();
        productAdapter = new ProductOrderAdapter(this, toBeBoughtProducts);
        productAdapter.setListener(this::calculateTotalPrice);
        productsListview.setAdapter(productAdapter);
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
            } else {
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

        if (bottomSheetDialog != null)
            bottomSheetDialog.dismiss();
    }

    @Override
    public void onMenuPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onAddPressed() {

    }

    @OnClick(R.id.nextButton)
    public void onClick(View view) {
        addProductToList();
    }

    /**
     * Adds a product to the list of the products to be bought.
     */
    private void addProductToList() {
        if (!allProductInfosArePresent()) {
            return;
        }

        mProduct = new Product();

        int quantity = Integer.valueOf(quantityField.getText().toString());
        mProduct.setID(selectedProduct.getID());
        mProduct.setLibelle(productField.getText().toString());
        mProduct.setPrixTTC(Double.valueOf(priceField.getText().toString()) * quantity);
        mProduct.setQte(quantity);
        mProduct.setSupplier(Integer.valueOf(selectedSupplier.getId()));
        toBeBoughtProducts.add(mProduct);
        productAdapter.notifyDataSetChanged();
        calculateTotalPrice();

        resetTextFields();
        mProduct = null;
    }

    /**
     * Invoked when the 'Validate' button is pressed.
     *
     * @param view
     */
    public void onConfirm(View view) {
        JSONArray summary = productAdapter.getSummary();
        postCreatePurchaseOrder(PhpAPI.createPurchaseOrder, summary);
    }

    /**
     * Overloaded method.
     *
     * @param url
     * @param orderInfos
     */
    void postCreatePurchaseOrder(String url, JSONArray orderInfos) {
        postCreatePurchaseOrder(url, JSONUtils.bundleWithTag(orderInfos, "data"));
    }

    /**
     * Overloaded method.
     *
     * @param url
     * @param orderInfos
     */
    void postCreatePurchaseOrder(String url, JSONObject orderInfos) {
        Request request = PhpAPI.createHTTPRequest(orderInfos, url, Method.POST);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(() -> Toast.makeText(PurchasesActivity.this,
                                call.request().toString(), Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(res);
                            int status = obj.getInt("success");

                            runOnUiThread(() -> {
                                if (status == 1) {
                                    setResult(ResultCodes.PURCHASE_ORDER_CREATED);
                                    finish();
                                }
                                else {
                                    runOnUiThread(() -> Toast.makeText(PurchasesActivity.this,
                                            "An error occured while registering your order. " + "Please try again.", Toast.LENGTH_SHORT)
                                            .show());
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Calculates the total of the TTC price of all to-be-bought products.
     */
    private void calculateTotalPrice() {
        double total = 0d;

        for (Product product : toBeBoughtProducts) {
            total += product.getPrixTTC();
        }

        priceLabel.setText(String.valueOf(total));
    }

    /**
     * @return true if all the informations of the to-be-bought product are
     * present, & false otherwise.
     */
    private boolean allProductInfosArePresent() {
        return Stream.of(supplierField, productField, quantityField, priceField)
                .map(EditText::getText)
                .map(Editable::toString)
                .noneMatch(String::isEmpty);
    }

    private void resetTextFields() {
        Stream.of(supplierField, productField, quantityField, priceField)
                .forEach(field -> field.setText(""));
    }
}
