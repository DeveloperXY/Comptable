package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ismailamrani.comptable.Adapters.SoldProductAdapter;
import com.example.ismailamrani.comptable.BarCodeScanner.IntentIntegrator;
import com.example.ismailamrani.comptable.BarCodeScanner.IntentResult;
import com.example.ismailamrani.comptable.R;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SalesActivity extends Activity {

    @Bind(R.id.productsListview)
    ListView productsListview;
    @Bind(R.id.barCodeField)
    EditText barCodeField;

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
        }
    }
}
