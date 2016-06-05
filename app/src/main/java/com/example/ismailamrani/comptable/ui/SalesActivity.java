package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ProductOrderAdapter;
import com.example.ismailamrani.comptable.barcodescanner.IntentIntegrator;
import com.example.ismailamrani.comptable.barcodescanner.IntentResult;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.base.WithDrawerActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.ui.DialogUtil;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.utils.http.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohammed Aouf ZOUAG on 27/04/2016.
 */
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
        productAdapter.setListener(new ProductOrderAdapter.SaleProductListener() {
            @Override
            public void onProductRemoved() {
                calculateTotalPrice();
            }
        });
        productsListview.setAdapter(productAdapter);
    }

    private void initializeUI() {
        setupActionBar();

        allowEditCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                priceField.setEnabled(isChecked);
            }
        });
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle(getString(R.string.sales));
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
        JSONArray qteSummary = productAdapter.getQuantitySummary();

        if (summary.length() == 0)
            Toast.makeText(this, R.string.empty_order_list, Toast.LENGTH_SHORT).show();
        else
            postCreateSaleOrder(PhpAPI.createSaleOrder,
                    JSONUtils.merge(
                            JSONUtils.merge(
                                    JSONUtils.bundleWithTag(summary, "data"),
                                    JSONUtils.bundleLocaleIDToJSON(mDatabaseAdapter.getCurrentLocaleID())
                            ),
                            JSONUtils.bundleWithTag(qteSummary, "qteData")
                    ));
    }

    /**
     * Overloaded method.
     *
     * @param url
     * @param orderInfos
     */
    void postCreateSaleOrder(String url, JSONObject orderInfos) {
        mLoadingDialog.show();

        sendHTTPRequest(url, orderInfos, Method.POST,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        setResult(ResultCodes.ORDER_CREATED);
                        finish();
                    }

                    @Override
                    public void onRequestFailed(int status, JSONObject response) {
                        final StringBuilder sb = new StringBuilder();
                        try {
                            JSONArray messages = response.getJSONArray("message");
                            for (int i = 0; i < messages.length(); i++) {
                                JSONObject message = messages.getJSONObject(i);
                                sb.append(message.getString("text"));
                                sb.append("\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showQuantityErrorDialog(sb.toString());
                            }
                        });
                    }
                });
    }

    private void showQuantityErrorDialog(String parsedMessages) {
        DialogUtil.showDialog(this, getString(R.string.error),
                getString(R.string.unavailable_quantities)
                        + parsedMessages, getString(R.string.ok), null);
    }

    /**
     * Adds a product to the list of the products to be sold.
     */
    private void addProductToList() {
        if (!allProductInfosArePresent() ||
                Integer.valueOf(quantityField.getText().toString()) <= 0) {
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
                .map(new Function<EditText, String>() {
                    @Override
                    public String apply(EditText field) {
                        return field.getText().toString();
                    }
                })
                .noneMatch(new Predicate<String>() {
                    @Override
                    public boolean test(String value) {
                        return value.isEmpty();
                    }
                });
    }

    private void resetTextFields() {
        Stream.of(barCodeField, quantityField, priceField)
                .forEach(new Consumer<EditText>() {
                    @Override
                    public void accept(EditText field) {
                        field.setText("");
                    }
                });
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

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    quantityField.setText("1");
                                    barCodeField.setText(mProduct.getCodeBarre());
                                    priceField.setText(mProduct.getPrixTTC() + "");
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
