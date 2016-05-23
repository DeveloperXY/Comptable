package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ProduitAdapter;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
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
    private DatabaseAdapter mDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits);
        ButterKnife.bind(this);

        mDatabaseAdapter = DatabaseAdapter.getInstance(this);

        setupActionBar();
        setupRevealTransition();
        fetchProducts();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Produits");
    }

    private void fetchProducts() {
        int localeID = mDatabaseAdapter.getCurrentLocaleID();
        JSONObject data = JSONUtils.bundleLocaleIDToJSON(localeID);

        sendHTTPRequest(PhpAPI.getProduit, data, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
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

                        ProduitAdapter adapter = new ProduitAdapter(ProductsActivity.this, productsList);
                        runOnUiThread(() -> productsListView.setAdapter(adapter));
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(ProductsActivity.this,
                                "No internet connection.",
                                Toast.LENGTH_LONG).show());
                    }
                });
    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddProductActivity.class));
    }
}
