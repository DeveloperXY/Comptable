package com.example.ismailamrani.comptable.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.ClientListActivity;
import com.example.ismailamrani.comptable.ui.FournisseurListActivity;
import com.example.ismailamrani.comptable.ui.ChargesActivity;
import com.example.ismailamrani.comptable.ui.ProductsActivity;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.ui.OrdersActivity;
import com.example.ismailamrani.comptable.ui.StockActivity;
import com.example.ismailamrani.comptable.utils.CalculateScreenSize;
import com.example.ismailamrani.comptable.utils.Orders;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ismail Amrani on 17/03/2016.
 */
public class HomeActivity extends ColoredStatusBarActivity {

    @Bind(R.id.localeLabel)
    TextView localeLabel;

    private DatabaseAdapter mDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CalculateScreenSize().CalculateScreenSize(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mDatabaseAdapter = DatabaseAdapter.getInstance(this);
        String currentLocale = mDatabaseAdapter.getUserAddress();
        int currentLocaleID = mDatabaseAdapter.getCurrentLocaleID();
        Log.i("LOCALE", "ID: " + currentLocaleID);
        localeLabel.setText(currentLocale);
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
        String orderType = "";

        switch (view.getId()) {
            case R.id.produit:
                targetActivity = ProductsActivity.class;
                break;
            case R.id.client:
                targetActivity = ClientListActivity.class;
                break;
            case R.id.chargee:
                targetActivity = ChargesActivity.class;
                break;
            case R.id.fournis:
                targetActivity = FournisseurListActivity.class;
                break;
            case R.id.stock:
                targetActivity = StockActivity.class;
                break;
            case R.id.achat:
                targetActivity = OrdersActivity.class;
                orderType = Orders.PURCHASE;
                break;
            case R.id.ventes:
                targetActivity = OrdersActivity.class;
                orderType = Orders.SALE;
                break;
            default:
                targetActivity = null;
        }

        Intent intent = new Intent(this, targetActivity);
        if (targetActivity == OrdersActivity.class)
            intent.putExtra("orderType", orderType);

        startActivity(intent);
    }
}
