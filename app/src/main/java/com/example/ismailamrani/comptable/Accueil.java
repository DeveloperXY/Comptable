package com.example.ismailamrani.comptable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Ismail Amrani on 17/03/2016.
 */
public class Accueil extends Activity {
    private static final String TAG = Accueil.class.getSimpleName();

    Context context;

    LinearLayout Produit,client,charge,fournisseur,stock,achat,ventes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        Log.d(TAG, TAG);

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
                startActivity(new Intent(context, Produis.class));
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,ClientList.class));
            }
        });

        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, NewCharge.class));
            }
        });
        fournisseur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FournisseurList.class));
            }
        });
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, AddLocalFile.class));
            }
        });
        achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, addSociete.class));

            }
        });

        ventes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Vente.class));

            }
        });
    }
}
