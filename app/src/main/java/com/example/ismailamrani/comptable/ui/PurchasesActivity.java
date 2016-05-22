package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ProductOrderAdapter;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.ui.dialogs.PurchaseChooserDialog;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchasesActivity extends ColoredStatusBarActivity {

    private Product selectedProduct;
    private Supplier selectedSupplier;

    private List<Product> products;
    private List<Supplier> suppliers;

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
        products = new ArrayList<>();
        suppliers = new ArrayList<>();

        productAdapter = new ProductOrderAdapter(this, toBeBoughtProducts);
        productAdapter.setListener(this::calculateTotalPrice);
        productsListview.setAdapter(productAdapter);
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Achats");
        mActionBar.disableAddButton();
    }

    @OnClick({R.id.supplierSpinner, R.id.productSpinner})
    public void onSpinnerClick(View view) {
        int spinnerID = view.getId();

        fetchDialogItems(
                spinnerID == R.id.productSpinner ?
                        PhpAPI.getProduit :
                        PhpAPI.getFournisseur,
                JSONUtils.bundleLocaleIDToJSON(DatabaseAdapter.getInstance(this)
                        .getCurrentLocaleID())
                , spinnerID);
    }

    void fetchDialogItems(String url, JSONObject data, int spinnerID) {
        sendHTTPRequest(url, data, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 0) {
                            Toast.makeText(PurchasesActivity.this, "Error",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            runOnUiThread(() -> prepareDialogData(response, spinnerID));
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(PurchasesActivity.this,
                                "No internet connection.",
                                Toast.LENGTH_LONG).show());
                    }
                });
    }

    private void prepareDialogData(JSONObject response, int spinnerID) {
        String hint;
        List<String> items = new ArrayList<>();

        if (spinnerID == R.id.productSpinner) {
            hint = "Search for products...";
            try {
                products = Product.parseProducts(response.getJSONArray("produit"));
                items = Stream.of(products)
                        .map(Product::getLibelle)
                        .collect(Collectors.toList());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            hint = "Search for suppliers...";
            try {
                suppliers = Supplier.parseSuppliers(response.getJSONArray("fournisseur"));
                items = Stream.of(suppliers)
                        .map(Supplier::getNom)
                        .collect(Collectors.toList());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        showChooserDialog(hint, items, spinnerID);
    }

    private void showChooserDialog(String searchHint, List<String> items, int spinnerID) {
        new PurchaseChooserDialog(this, spinnerID)
                .whoseItemsAre(items)
                .whoseSearchHintIs(searchHint)
                .runWhenItemSelected(item -> {
                    if (spinnerID == R.id.productSpinner) {
                        // A product has been selected
                        selectedProduct = Stream.of(products)
                                .filter(p -> p.getLibelle().equals(item))
                                .findFirst()
                                .get();
                        productField.setText(selectedProduct.getLibelle());
                    } else {
                        // A supplier has been selected
                        selectedSupplier = Stream.of(suppliers)
                                .filter(s -> s.getNom().equals(item))
                                .findFirst()
                                .get();
                        supplierField.setText(selectedSupplier.getNom());
                    }
                })
                .show();
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

        if (summary.length() == 0)
            Toast.makeText(this, "Your order list is empty.", Toast.LENGTH_SHORT).show();
        else
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
        Log.i("JSON", orderInfos.toString());
        sendHTTPRequest(url, orderInfos, Method.POST,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 1) {
                            setResult(ResultCodes.ORDER_CREATED);
                            finish();
                        } else {
                            runOnUiThread(() -> Toast.makeText(PurchasesActivity.this,
                                    "An error occured while registering your order. " +
                                            "Please try again.", Toast.LENGTH_SHORT)
                                    .show());
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(PurchasesActivity.this,
                                "Unknown error", Toast.LENGTH_LONG).show());
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
