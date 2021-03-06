package com.example.ismailamrani.comptable.ui.base;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.DrawerRecyclerAdapter;
import com.example.ismailamrani.comptable.models.DrawerItem;
import com.example.ismailamrani.comptable.ui.AccountingDetailsActivity;
import com.example.ismailamrani.comptable.ui.AccountingHomeActivity;
import com.example.ismailamrani.comptable.ui.ChargesActivity;
import com.example.ismailamrani.comptable.ui.ClientsActivity;
import com.example.ismailamrani.comptable.ui.OrdersActivity;
import com.example.ismailamrani.comptable.ui.ProductsActivity;
import com.example.ismailamrani.comptable.ui.StockActivity;
import com.example.ismailamrani.comptable.ui.SuppliersActivity;
import com.example.ismailamrani.comptable.ui.startup.HomeActivity;
import com.example.ismailamrani.comptable.ui.startup.LoginActivity;
import com.example.ismailamrani.comptable.utils.parsing.Orders;
import com.example.ismailamrani.comptable.utils.ui.ActivityTransition;
import com.example.ismailamrani.comptable.utils.ui.DrawerOrder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Mohammed Aouf ZOUAG on 25/05/2016.
 * <p>
 * Extending this activity plugs in a DrawerLayout that is ready to use.
 */
public abstract class WithDrawerActivity extends ColoredStatusBarActivity
        implements DrawerOrder {

    @Bind(R.id.drawerRecyclerView)
    protected RecyclerView drawerRecyclerView;

    @Bind(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;

    private DrawerRecyclerAdapter mDrawerRecyclerAdapter;
    protected String currentUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserType = mDatabaseAdapter.getUserType();
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
        mDrawerRecyclerAdapter.setDrawerClickListener(new DrawerRecyclerAdapter.DrawerClickListener() {
            @Override
            public void onItemClicked(DrawerItem drawerItem, View clickedImage) {
                Intent intent = drawerItem.getIntent();
                if (intent != null) {
                    // TODO: hack; this code will break if you move the HomeActivity class into another package
                    if (intent.getComponent().getShortClassName().equals(".ui.startup.HomeActivity")) {
                        ActivityCompat.finishAffinity(WithDrawerActivity.this);
                        startActivity(intent);
                    } else {
                        activityShouldFinish();

                        intent.putExtra("imageRes", drawerItem.getIcon());
                        ActivityTransition.startActivityWithSharedElement(
                                WithDrawerActivity.this, intent, clickedImage, "menuAnim");
                    }

                    drawerLayout.closeDrawer(drawerRecyclerView);
                } else {
                    logout();
                }
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
        // The starting index will be two, counting the header view, & the home page view
        int selectedIndex = 2;

        switch (getActivity()) {
            case STOCK:
                break;
            case SALES:
                selectedIndex += 1;
                break;
            case PURCHASES:
                selectedIndex += 2;
                break;
            case PRODUCTS:
                selectedIndex += 3;
                break;
            case CLIENTS:
                selectedIndex += 4;
                break;
            case SUPPLIERS:
                selectedIndex += 5;
                break;
            case CHARGES:
                selectedIndex += 6;
                break;
            case COMPTABILITE:
                selectedIndex += 7;
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
                targetActivity = HomeActivity.class;
                break;
            case 1:
                targetActivity = StockActivity.class;
                break;
            case 2:
                targetActivity = OrdersActivity.class;
                orderType = Orders.SALE;
                break;
            case 3:
                targetActivity = OrdersActivity.class;
                orderType = Orders.PURCHASE;
                break;
            case 4:
                targetActivity = ProductsActivity.class;
                break;
            case 5:
                targetActivity = ClientsActivity.class;
                break;
            case 6:
                targetActivity = SuppliersActivity.class;
                break;
            case 7:
                targetActivity = ChargesActivity.class;
                break;
            case 8:
                targetActivity = currentUserType.startsWith("e") ?
                        AccountingDetailsActivity.class : AccountingHomeActivity.class;
                break;
            case 9:
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
     * Logs the current user out.
     */
    protected void logout() {
        mDatabaseAdapter.logout();

        ActivityCompat.finishAffinity(this);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.NONE;
    }
}
