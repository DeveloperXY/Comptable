package com.example.ismailamrani.comptable.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ismailamrani.comptable.Adapters.SoldProductAdapter;
import com.example.ismailamrani.comptable.R;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SalesActivity extends Activity {

    @Bind(R.id.productsListview)
    ListView productsListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_layout);
        ButterKnife.bind(this);

        List<String> data = Arrays.asList("IPhone 6 Plus", "Samsung Galaxy S7",
                "Samsung Trend Plus", "IPhone 5S");
        productsListview.setAdapter(new SoldProductAdapter(this, data));
    }
}
