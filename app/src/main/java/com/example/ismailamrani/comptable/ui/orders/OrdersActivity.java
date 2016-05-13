package com.example.ismailamrani.comptable.ui.orders;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.ui.orders.fragments.OrderDetailsFragment;
import com.example.ismailamrani.comptable.ui.orders.fragments.OrdersListFragment;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrdersActivity extends ColoredStatusBarActivity
        implements OrdersListFragment.OrderListFragListener,
        OrderDetailsFragment.OrderDetailsFragListener {

    @Bind(R.id.MyActionBar)
    protected OGActionBar mActionBar;

    private String currentOrderType;
    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);

        setupOrdersType();
        setupActionBar();

        displayFragment(0); // always display first fragment at startup
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment instanceof OrderDetailsFragment)
            displayFragment(0);
        else
            super.onBackPressed();
    }

    /**
     * @param position of the fragment to show
     */
    private void displayFragment(int position) {
        displayFragment(position, null);
    }

    private void displayFragment(int position, Order order) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                fragment = OrdersListFragment.newInstance(currentOrderType);
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                break;
            case 1:
                fragment = order != null ? OrderDetailsFragment.newInstance(order.getId()) :
                        new OrderDetailsFragment();
                ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
                break;
        }

        ft.replace(R.id.frame_container, fragment, "fragment" + position);
        ft.commit();
    }

    protected void setupActionBar() {
        mActionBar.setActionBarListener(this);
        mActionBar.setTitle(currentOrderType.equals("PURCHASE") ?
                "Commandes achats" : "Commandes ventes");
    }

    /**
     * Retrieves the type of orders to be currently displayed.
     */
    private void setupOrdersType() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                currentOrderType = data.getString("orderType");
            }
        }
    }

    @Override
    public void onAddPressed() {
        startActivity(new Intent(this, "PURCHASE".equals(currentOrderType) ?
                PurchasesActivity.class : SalesActivity.class));
    }

    /**
     * Fetches orders' data on behalf of the fragment.
     *
     * @param url
     * @param listener
     */
    @Override
    public void fetchOrders(String url, RequestListener listener) {
        sendHTTPRequest(url, null, Method.GET, listener);
    }

    @Override
    public void onOrderItemPressed(Order order) {
        currentOrder = order;
        displayFragment(1, order);
    }

    @Override
    public void fetchOrderDetails(String url, RequestListener requestListener) {

    }
}
