package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ChargeAdapter;
import com.example.ismailamrani.comptable.models.Charge;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargesActivity extends ColoredStatusBarActivity {

    @Bind(R.id.chargesRecyclerView)
    RecyclerView chargesRecyclerView;

    private ChargeAdapter mChargeAdapter;
    private List<Charge> mCharges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);
        ButterKnife.bind(this);

        setupActionBar();
        setupRecyclerView();
    }

    private void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle("Charges");
    }

    private void setupRecyclerView() {
        mCharges = new ArrayList<>();
        chargesRecyclerView.setHasFixedSize(true);
        chargesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chargesRecyclerView.addItemDecoration(new SpacesItemDecoration(4));

        refresh();
    }

    private void refresh() {
        fetchChargeItems();
    }

    private void fetchChargeItems() {
        mCharges = Arrays.asList(
                new Charge(1, "Description 1", 100, "2016-10-24", 3),
                new Charge(2, "Description 2", 200, "2016-10-24", 3),
                new Charge(3, "Description 3", 300, "2016-10-24", 3),
                new Charge(4, "Description 4", 400, "2016-10-24", 3),
                new Charge(5, "Description 5", 500, "2016-10-24", 3));
        populateRecyclerView();
    }

    private void populateRecyclerView() {
        if (mChargeAdapter == null) {
            mChargeAdapter = new ChargeAdapter(this, mCharges);
            chargesRecyclerView.setAdapter(mChargeAdapter);
        } else
            mChargeAdapter.animateTo(mCharges);
    }
}
