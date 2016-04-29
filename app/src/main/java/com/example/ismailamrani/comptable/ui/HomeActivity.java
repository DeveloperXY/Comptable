package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;

/**
 * Created by Ismail Amrani on 17/03/2016.
 */
public class HomeActivity extends Activity {
    Context context;
    LinearLayout Produit,client,charge,fournisseur,stock,achat,ventes;

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        databaseAdapter = DatabaseAdapter.getInstance(this);

        if(!isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        context = this;

        Produit = (LinearLayout) findViewById(R.id.Produit);
        client = (LinearLayout) findViewById(R.id.client);
        charge = (LinearLayout) findViewById(R.id.chargee);
        fournisseur = (LinearLayout) findViewById(R.id.fournis);
        stock = (LinearLayout) findViewById(R.id.stock);
        achat = (LinearLayout) findViewById(R.id.achat);
        ventes= (LinearLayout) findViewById(R.id.ventes);
        Produit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ProduisActivity.class));
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,ClientListActivity.class));
            }
        });

        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, NewChargeActivity.class));
            }
        });
        fournisseur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FournisseurListActivity.class));
            }
        });
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, AddLocalFileActivity.class));
            }
        });
        achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, addSocieteActivity.class));

            }
        });

        ventes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SalesActivity.class));

            }
        });
    }

    /**
     * @return true if a user is already logged in, false otherwise.
     */
    private boolean isUserLoggedIn() {
        return databaseAdapter.getLoggedUser() != null;
    }
}
