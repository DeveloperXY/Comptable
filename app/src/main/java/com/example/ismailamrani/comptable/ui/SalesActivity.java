package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.Adapters.SoldProductAdapter;
import com.example.ismailamrani.comptable.BarCodeScanner.IntentIntegrator;
import com.example.ismailamrani.comptable.BarCodeScanner.IntentResult;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ServiceWeb.PhpAPI;
import com.example.ismailamrani.comptable.utils.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
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

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_layout);
        ButterKnife.bind(this);

        List<String> data = Arrays.asList("IPhone 6 Plus", "Samsung Galaxy S7",
                "Samsung Trend Plus", "IPhone 5S");
        productsListview.setAdapter(new SoldProductAdapter(this, data));
    }

    @OnClick(R.id.addBarCodeBtn)
    public void onClick() {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            barCodeField.setText(scanContent);

            try {
                JSONObject params = new JSONObject();
                params.put("barcode", scanContent);

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
