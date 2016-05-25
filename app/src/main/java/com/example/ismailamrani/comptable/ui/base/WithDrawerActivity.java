package com.example.ismailamrani.comptable.ui.base;

import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.DrawerRecyclerAdapter;
import com.example.ismailamrani.comptable.utils.DrawerOrder;

import butterknife.Bind;

/**
 * Created by Mohammed Aouf ZOUAG on 25/05/2016.
 * <p>
 * Represents an activity with a DrawerLayout.
 */
public abstract class WithDrawerActivity extends ColoredStatusBarActivity
        implements DrawerOrder {

    @Bind(R.id.drawerRecyclerView)
    protected RecyclerView drawerRecyclerView;

    @Bind(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;

    private DrawerRecyclerAdapter mDrawerRecyclerAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        setupNavigationDrawer();
    }

    @Override
    public void onMenuPressed() {
        drawerLayout.openDrawer(drawerRecyclerView);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawerRecyclerView))
            drawerLayout.closeDrawer(drawerRecyclerView);
        else super.onBackPressed();
    }

    protected void setupNavigationDrawer() {
        String[] titles = getResources().getStringArray(R.array.navDrawerItems);
        TypedArray icons = getResources().obtainTypedArray(R.array.navDrawerIcons);

        mDrawerRecyclerAdapter = new DrawerRecyclerAdapter(titles, icons, this);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerRecyclerView.setAdapter(mDrawerRecyclerAdapter);
        setupSelectedDrawerItem();
    }

    /**
     * Sets the selected item of the navigation drawer based on activity order.
     */
    private void setupSelectedDrawerItem() {
        // The starting index will be one, counting the header view
        int selectedIndex = 1;

        switch (getActivity()) {
            case PRODUCTS:
                break;
            case SALES:
                selectedIndex += 1;
                break;
            case PURCHASES:
                selectedIndex += 2;
                break;
            case CLIENTS:
                selectedIndex += 3;
                break;
            case STOCK:
                selectedIndex += 4;
                break;
            case SUPPLIERS:
                selectedIndex += 5;
                break;
            case CHARGES:
                selectedIndex += 6;
                break;
            default:
                selectedIndex = -1;
        }

        mDrawerRecyclerAdapter.setSelectedItem(selectedIndex);
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.NONE;
    }
}
