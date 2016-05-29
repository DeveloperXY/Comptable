package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.LocaleAdapter;
import com.example.ismailamrani.comptable.models.Local;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountingHomeActivity extends AnimatedActivity {

    @Bind(R.id.localeListView)
    ListView localeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptabilite_home);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
        setupLocalesListView();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Total Profit");
        mActionBar.disableAddButton();
    }

    private void setupLocalesListView() {
        List<Local> locales = mDatabaseAdapter.retrieveCurrentLocales();
        LocaleAdapter localeAdapter = new LocaleAdapter(this, locales);
        localeAdapter.setListener(localeID -> {
            Intent intent = new Intent(this, AccountingDetailsActivity.class);
            intent.putExtra("localeID", localeID);
            startActivity(intent);
        });
        localeListView.setAdapter(localeAdapter);
    }
}
