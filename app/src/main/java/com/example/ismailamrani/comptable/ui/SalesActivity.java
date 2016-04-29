package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.SoldProductAdapter;
import com.example.ismailamrani.comptable.barcodescanner.IntentIntegrator;
import com.example.ismailamrani.comptable.barcodescanner.IntentResult;
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


public class SalesActivity extends Activity {

    @Bind(R.id.productsListview)
    ListView productsListview;
    @Bind(R.id.barCodeField)
    EditText barCodeField;
    @Bind(R.id.quantityField)
    EditText quantityField;
    @Bind(R.id.priceField)
    EditText priceField;

    private Product mProduct;
    private List<Product> toBeSoldProducts;

    private OkHttpClient client = new OkHttpClient();
    private SoldProductAdapter soldProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_layout);
        ButterKnife.bind(this);

        toBeSoldProducts = new ArrayList<>();
        soldProductAdapter = new SoldProductAdapter(this, toBeSoldProducts);
        productsListview.setAdapter(soldProductAdapter);
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
     * Adds a product to the list of the products to be sold.
     */
    private void addProductToList() {
        toBeSoldProducts.add(mProduct);
        soldProductAdapter.notifyDataSetChanged();
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
}
