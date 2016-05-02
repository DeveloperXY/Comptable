package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.SoldProductAdapter;
import com.example.ismailamrani.comptable.barcodescanner.IntentIntegrator;
import com.example.ismailamrani.comptable.barcodescanner.IntentResult;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBarInterface;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.utils.Method;
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


public class SalesActivity extends ColoredStatusBarActivity
        implements OGActionBarInterface {

    private OGActionBar mActionBar;

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

    private OkHttpClient client = new OkHttpClient();
    private SoldProductAdapter soldProductAdapter;
    private TextWatcher quantityWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_layout);
        ButterKnife.bind(this);

        initializeUI();
        attachTextWatchers();

        toBeSoldProducts = new ArrayList<>();
        soldProductAdapter = new SoldProductAdapter(this, toBeSoldProducts);
        soldProductAdapter.setListener(this::calculateTotalPrice);
        productsListview.setAdapter(soldProductAdapter);
    }

    private void initializeUI() {
        setupActionBar();

        allowEditCheckbox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> priceField.setEnabled(isChecked));
    }

    private void setupActionBar() {
        mActionBar = (OGActionBar) findViewById(R.id.MyActionBar);
        mActionBar.setActionBarListener(this);
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
    public void onButtonClick(View view) {
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
    public void onConfirmSale(View view) {
        JSONArray summary = soldProductAdapter.getSummary();
        postCreateSaleOrder(PhpAPI.createSaleOrder, summary);
    }

    void postCreateSaleOrder(String url, JSONArray orderInfos) {
        JSONObject data = new JSONObject();
        try {
            data.put("data", orderInfos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postCreateSaleOrder(url, data);
    }

    void postCreateSaleOrder(String url, JSONObject userCredentials) {
        Request request = PhpAPI.createHTTPRequest(userCredentials, url, Method.POST);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(() -> Toast.makeText(SalesActivity.this,
                                call.request().toString(), Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(res);
                            int status = obj.getInt("success");

                            runOnUiThread(() -> Toast.makeText(SalesActivity.this,
                                    status == 1 ? "SUCCESS" : "FAILURE",
                                    Toast.LENGTH_SHORT).show());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Adds a product to the list of the products to be sold.
     */
    private void addProductToList() {
        if (!allProductInfosArePresent()) {
            return;
        }

        int quantity = Integer.valueOf(quantityField.getText().toString());
        mProduct.setPrixTTC(mProduct.getPrixTTC() * quantity);
        mProduct.setQte(quantity);
        toBeSoldProducts.add(mProduct);
        soldProductAdapter.notifyDataSetChanged();
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
     *
     * @param url             destination URL
     * @param userCredentials the user's informations
     * @throws IOException
     */
    void postGetProduct(String url, JSONObject userCredentials) throws IOException {
        Request request = PhpAPI.createHTTPRequest(userCredentials, url, Method.POST);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(() -> Toast.makeText(SalesActivity.this,
                                call.request().toString(), Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(res);
                            int resp = obj.getInt("success");

                            runOnUiThread(() -> {
                                if (resp == 0)
                                    Toast.makeText(SalesActivity.this,
                                            "Unregistered product.",
                                            Toast.LENGTH_LONG).show();
                                else {
                                    try {
                                        JSONArray productList = obj.getJSONArray("produit");
                                        JSONObject product = productList.getJSONObject(0);
                                        mProduct = new Product(product);

                                        quantityField.setText("1");
                                        barCodeField.setText(mProduct.getCodeBarre());
                                        priceField.setText(mProduct.getPrixTTC() + "");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onMenuPressed() {

    }

    @Override
    public void onAddPressed() {

    }
}
