package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ProductsAdapter;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ismail Amrani on 23/03/2016.
 */
public class ProductsActivity extends AnimatedActivity {

    @Bind(R.id.List)
    ListView productsListView;

    private List<Product> productsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
        fetchProducts();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Produits");
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.PRODUCTS;
    }

    private void fetchProducts() {
        int localeID = mDatabaseAdapter.getCurrentLocaleID();
        JSONObject data = JSONUtils.bundleLocaleIDToJSON(localeID);

        mLoadingDialog.show();

        sendHTTPRequest(PhpAPI.getProduit, data, Method.GET,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        try {
                            JSONArray listproduits = response.getJSONArray("produit");

                            for (int i = 0; i < listproduits.length(); i++) {
                                JSONObject usr = listproduits.getJSONObject(i);
                                Product itm = new Product();
                                itm.setID(Integer.parseInt(usr.getString("idp")));
                                itm.setLibelle(usr.getString("libelle"));
                                itm.setPrixTTC(Double.parseDouble(usr.getString("prixTTC")));
                                itm.setQte(Integer.parseInt(usr.getString("qte")));
                                itm.setPhoto(PhpAPI.IpBackend_IMAGES + usr.getString("photo"));

                                productsList.add(itm);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ProductsAdapter adapter = new ProductsAdapter(ProductsActivity.this, productsList);
                        adapter.setProductListener(new ProductsAdapter.ProductListener() {
                            @Override
                            public void onDeleteProduct(String url, JSONObject params, Method method) {
                                sendHTTPRequest(url, params, method, new DeleteRequestListener());
                            }
                        });
                        runOnUiThread(() -> productsListView.setAdapter(adapter));
                    }
                });
    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddProductActivity.class));
    }

    class DeleteRequestListener extends SuccessRequestListener {
        @Override
        public void onRequestSucceeded(JSONObject response) {
            runOnUiThread(() -> {
                Toast.makeText(ProductsActivity.this, "Product deleted.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProductsActivity.this, ProductsActivity.class));
            });
        }

        @Override
        public void onRequestFailed(int status, JSONObject response) {
            runOnUiThread(() -> Toast.makeText(ProductsActivity.this, "Unknown error.",
                    Toast.LENGTH_LONG).show());
        }
    }
}
