package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.UsedMethodes.CalculateScreenSize;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ismail Amrani on 17/03/2016.
 */
public class HomeActivity extends Activity {

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CalculateScreenSize().CalculateScreenSize(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        databaseAdapter = DatabaseAdapter.getInstance(this);

        if (!isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /**
     * Starts a new activity based on the clicked-upon layout.
     * @param view that was clicked.
     */
    @OnClick({R.id.produit, R.id.client, R.id.chargee, R.id.stock,
            R.id.ventes, R.id.achat, R.id.fournis})
    public void OnClick(View view) {
        Class<?> targetActivity;
        switch (view.getId()) {
            case R.id.produit:
                targetActivity = ProduisActivity.class;
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
                targetActivity = AddLocalFileActivity.class;
                break;
            case R.id.achat:
                targetActivity = addSocieteActivity.class;
                break;
            case R.id.ventes:
                targetActivity = SalesActivity.class;
                break;
            default:
                targetActivity = null;
        }

        startActivity(new Intent(this, targetActivity));
    }

    /**
     * @return true if a user is already logged in, false otherwise.
     */
    private boolean isUserLoggedIn() {
        return databaseAdapter.getLoggedUser() != null;
    }
}
