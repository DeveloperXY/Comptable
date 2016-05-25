package com.example.ismailamrani.comptable.ui.base;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.DrawerRecyclerAdapter;
import com.example.ismailamrani.comptable.models.DrawerItem;
import com.example.ismailamrani.comptable.ui.ChargesActivity;
import com.example.ismailamrani.comptable.ui.ClientListActivity;
import com.example.ismailamrani.comptable.ui.FournisseurListActivity;
import com.example.ismailamrani.comptable.ui.OrdersActivity;
import com.example.ismailamrani.comptable.ui.ProductsActivity;
import com.example.ismailamrani.comptable.ui.StockActivity;
import com.example.ismailamrani.comptable.utils.DrawerOrder;
import com.example.ismailamrani.comptable.utils.Orders;

import java.util.ArrayList;
import java.util.List;

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

        List<DrawerItem> drawerItems = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            Intent intent = getIntentBasedOnPosition(i);
            int icon = icons.getResourceId(i, -1);

            drawerItems.add(new DrawerItem(title, intent, icon));
        }

        icons.recycle();

        mDrawerRecyclerAdapter = new DrawerRecyclerAdapter(drawerItems);
        mDrawerRecyclerAdapter.setDrawerClickListener(drawerItem -> {
            Intent intent = drawerItem.getIntent();
            if (intent != null) {
                activityShouldFinish();
                startActivity(intent);
            }
        });
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

        mDrawerRecyclerAdapter.setSelectedIndex(selectedIndex);
    }
    /**
     * @param position of the drawer item
     * @return the intent corresponding to the passed-in position of this drawer item.
     */
    private Intent getIntentBasedOnPosition(int position) {
        Class<?> targetActivity;
        String orderType = ""; // the type of orders to be shown

        switch (position) {
            case 0:
                targetActivity = ProductsActivity.class;
                break;
            case 1:
                targetActivity = OrdersActivity.class;
                orderType = Orders.SALE;
                break;
            case 2:
                targetActivity = OrdersActivity.class;
                orderType = Orders.PURCHASE;
                break;
            case 3:
                targetActivity = ClientListActivity.class;
                break;
            case 4:
                targetActivity = StockActivity.class;
                break;
            case 5:
                targetActivity = FournisseurListActivity.class;
                break;
            case 6:
                targetActivity = ChargesActivity.class;
                break;
            case 7:
            default:
                targetActivity = null;
                break;
        }

        Intent intent = null;
        if (targetActivity != null) {
            intent = new Intent(this, targetActivity);
            if (targetActivity == OrdersActivity.class)
                intent.putExtra("orderType", orderType);
        }

        return intent;
    }

    /**
     * Helps identifying each implementing activity.
     */
    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.NONE;
    }
}
