package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ProductOrderAdapter;
import com.example.ismailamrani.comptable.barcodescanner.IntentIntegrator;
import com.example.ismailamrani.comptable.barcodescanner.IntentResult;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.base.WithDrawerActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
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


public class SalesActivity extends WithDrawerActivity {

    @Bind(R.id.productsListview)
    ListView productsListview;
    @Bind(R.id.barCodeField)
    EditText barCodeField;
    @Bind(R.id.quantityField)
    EditText quantityField;
    @Bind(R.id.priceField)
    EditText priceField;
    @Bind(R.id.priceLabel)
    TextView priceLabel;
    @Bind(R.id.allowEditCheckbox)
    CheckBox allowEditCheckbox;

    private Product mProduct;
    private List<Product> toBeSoldProducts;

    private ProductOrderAdapter productAdapter;
    private TextWatcher quantityWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_layout);
        ButterKnife.bind(this);

        initializeUI();
        attachTextWatchers();

        toBeSoldProducts = new ArrayList<>();
        productAdapter = new ProductOrderAdapter(this, toBeSoldProducts);
        productAdapter.setListener(this::calculateTotalPrice);
        productsListview.setAdapter(productAdapter);
    }

    private void initializeUI() {
        setupActionBar();

        allowEditCheckbox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> priceField.setEnabled(isChecked));
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Ventes");
    }

    /**
     * Sets TextWatchers on their corresponding text fields.
     */
    private void attachTextWatchers() {
        initializeTextWatchers();

        quantityField.addTextChangedListener(quantityWatcher);
    }

    private void initializeTextWatchers() {
        quantityWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if a product has been scanned
                if (mProduct != null) {
                    String qte = s.toString();
                    // Check if there was in deed a new quantity
                    if (!qte.isEmpty()) {
                        // Check if the new quantity has a valid format
                        if (qte.matches("(\\d+)")) {
                            int newQuantity = Integer.valueOf(qte);
                            double newPrice = mProduct.getPrixTTC() * newQuantity;
                            priceField.setText(String.valueOf(newPrice));
                        }
                    } else {
                        // Show original price
                        priceField.setText(String.valueOf(mProduct.getPrixTTC()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    @OnClick({R.id.addBarCodeBtn, R.id.nextButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBarCodeBtn:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;
            case R.id.nextButton:
                addProductToList();
                break;
        }
    }

    /**
     * Invoked when the 'Validate' button is pressed.
     *
     * @param view
     */
    public void onConfirm(View view) {
        JSONArray summary = productAdapter.getSummary();

        if (summary.length() == 0)
            Toast.makeText(this, "Your order list is empty.", Toast.LENGTH_SHORT).show();
        else
            postCreateSaleOrder(PhpAPI.createSaleOrder, summary);
    }

    /**
     * Overloaded method.
     *
     * @param url
     * @param orderInfos
     */
    void postCreateSaleOrder(String url, JSONArray orderInfos) {
        postCreateSaleOrder(url, JSONUtils.merge(
                JSONUtils.bundleWithTag(orderInfos, "data"),
                JSONUtils.bundleLocaleIDToJSON(mDatabaseAdapter.getCurrentLocaleID())
        ));
    }

    /**
     * Overloaded method.
     *
     * @param url
     * @param orderInfos
     */
    void postCreateSaleOrder(String url, JSONObject orderInfos) {
        sendHTTPRequest(url, orderInfos, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.ORDER_CREATED);
                        finish();
                    }
                });
    }

    /**
     * Adds a product to the list of the products to be sold.
     */
    private void addProductToList() {
        if (!allProductInfosArePresent() &&
                Integer.valueOf(quantityField.getText().toString()) > 0) {
            return;
        }

        int quantity = Integer.valueOf(quantityField.getText().toString());
        mProduct.setPrixTTC(mProduct.getPrixTTC() * quantity);
        mProduct.setQte(quantity);
        toBeSoldProducts.add(mProduct);
        productAdapter.notifyDataSetChanged();
        calculateTotalPrice();

        resetTextFields();
        mProduct = null;
    }

    /**
     * Calculates the total of the TTC price of all to-be-sold products.
     */
    private void calculateTotalPrice() {
        double total = 0d;

        for (Product product : toBeSoldProducts) {
            total += product.getPrixTTC();
        }

        priceLabel.setText(String.valueOf(total));
    }

    /**
     * @return true if all the informations of the to-be-added product are
     * present, & false otherwise.
     */
    private boolean allProductInfosArePresent() {
        return Stream.of(barCodeField, quantityField, priceField)
                .map(EditText::getText)
                .map(Editable::toString)
                .noneMatch(String::isEmpty);
    }

    private void resetTextFields() {
        Stream.of(barCodeField, quantityField, priceField)
                .forEach(field -> field.setText(""));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningResult != null) {
            String scannedBarcode = scanningResult.getContents();

            try {
                JSONObject params = new JSONObject();
                params.put("barcode", scannedBarcode);

                postGetProduct(PhpAPI.getProduitByBarcode, params);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the scanned product's informations based on its bar code.
     */
    void postGetProduct(String url, JSONObject data) throws IOException {
        sendHTTPRequest(url, data, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        try {
                            JSONArray productList = response.getJSONArray("produit");
                            JSONObject product = productList.getJSONObject(0);
                            mProduct = new Product(product);

                            runOnUiThread(() -> {
                                quantityField.setText("1");
                                barCodeField.setText(mProduct.getCodeBarre());
                                priceField.setText(mProduct.getPrixTTC() + "");
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
