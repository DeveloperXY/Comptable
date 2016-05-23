package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.webservice.PhpAPI;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ismail Amrani on 25/03/2016.
 */
public class ProductDetailsActivity extends ColoredStatusBarActivity {

    @Bind(R.id.Imageaff)
    ImageView Image;
    @Bind(R.id.PrixHTaff)
    TextView PrixHT;
    @Bind(R.id.PrixTTCaff)
    TextView PrixTTC;
    @Bind(R.id.Codeaff)
    TextView Code;
    @Bind(R.id.Stockaff)
    TextView Stock;
    @Bind(R.id.Libelleaff)
    TextView Libelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produit_info);
        ButterKnife.bind(this);

        setupActionBar();
        Product product = getIntent().getParcelableExtra("product");

        Picasso.with(getApplicationContext())
                .load(PhpAPI.IpBackend_IMAGES + product.getPhoto())
                .into(Image);
        Libelle.setText(product.getLibelle());
        PrixHT.setText(product.getPrixHT() + "");
        PrixTTC.setText(product.getPrixTTC() + "");
        Code.setText(product.getCodeBarre());
        Stock.setText(product.getQte() + "");
        mActionBar.setTitle(product.getLibelle());
    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, AddProductActivity.class));
    }
}
