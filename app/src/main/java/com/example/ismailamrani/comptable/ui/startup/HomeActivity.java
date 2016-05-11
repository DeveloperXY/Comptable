package com.example.ismailamrani.comptable.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.ClientListActivity;
import com.example.ismailamrani.comptable.ui.FournisseurListActivity;
import com.example.ismailamrani.comptable.ui.NewChargeActivity;
import com.example.ismailamrani.comptable.ui.ProductsActivity;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.ui.orders.PurchaseOrdersActivity;
import com.example.ismailamrani.comptable.ui.orders.SaleOrdersActivity;
import com.example.ismailamrani.comptable.ui.stock.StockActivity;
import com.example.ismailamrani.comptable.utils.CalculateScreenSize;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ismail Amrani on 17/03/2016.
 */
public class HomeActivity extends ColoredStatusBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CalculateScreenSize().CalculateScreenSize(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    /**
     * Starts a new activity based on the clicked-upon layout.
     *
     * @param view that was clicked.
     */
    @OnClick({R.id.produit, R.id.client, R.id.chargee, R.id.stock,
            R.id.ventes, R.id.achat, R.id.fournis})
    public void OnClick(View view) {
        Class<?> targetActivity;
        switch (view.getId()) {
            case R.id.produit:
                targetActivity = ProductsActivity.class;
                break;
            case R.id.client:
                targetActivity = ClientListActivity.class;
                break;
            case R.id.chargee:
                targetActivity = NewChargeActivity.class;
                break;
            case R.id.fournis:
                targetActivity = FournisseurListActivity.class;
                break;
            case R.id.stock:
                targetActivity = StockActivity.class;
                break;
            case R.id.achat:
                targetActivity = PurchaseOrdersActivity.class;
                break;
            case R.id.ventes:
                targetActivity = SaleOrdersActivity.class;
                break;
            default:
                targetActivity = null;
        }

        startActivity(new Intent(this, targetActivity));
    }
}
