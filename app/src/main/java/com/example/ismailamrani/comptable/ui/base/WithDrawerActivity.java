package com.example.ismailamrani.comptable.ui.base;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.DrawerRecyclerAdapter;

import butterknife.Bind;

/**
 * Created by Mohammed Aouf ZOUAG on 25/05/2016.
 *
 * Represents an activity with a DrawerLayout.
 */
public abstract class WithDrawerActivity extends ColoredStatusBarActivity {

    @Bind(R.id.drawerRecyclerView)
    protected RecyclerView drawerRecyclerView;

    @Bind(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupNavigationDrawer();
    }

    @Override
    public void onMenuPressed() {
        drawerLayout.openDrawer(drawerRecyclerView);
    }

    private void setupNavigationDrawer() {
        String[] titles = getResources().getStringArray(R.array.navDrawerItems);
        TypedArray icons = getResources().obtainTypedArray(R.array.navDrawerIcons);

        DrawerRecyclerAdapter adapter = new DrawerRecyclerAdapter(titles, icons, this);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerRecyclerView.setAdapter(adapter);
    }
}
