package com.example.ismailamrani.comptable.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;
import com.example.ismailamrani.comptable.ui.fragments.OrderDetailsFragment;
import com.example.ismailamrani.comptable.ui.fragments.OrdersListFragment;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.parsing.Orders;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.utils.http.PhpAPI;

import org.json.JSONObject;

import butterknife.ButterKnife;

public class OrdersActivity extends AnimatedActivity
        implements OrdersListFragment.OrderListFragListener,
        OrderDetailsFragment.OrderDetailsFragListener {

    private static final int REQUEST_CREATE_ORDER = 1;

    private String currentOrderType;
    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);

        currentOrderType = retrieveOrdersType();
        setupActionBar();
        setupRevealTransition();

        displayFragment(0); // always display first fragment at startup
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_container);
        if (drawerLayout.isDrawerOpen(drawerRecyclerView))
            drawerLayout.closeDrawer(drawerRecyclerView);
        else {
            if (fragment instanceof OrderDetailsFragment)
                displayFragment(0);
            else
                super.onBackPressed();
        }
    }

    @Override
    public ActivityOrder getActivity() {
        return currentOrderType.equals(Orders.PURCHASE) ?
                ActivityOrder.PURCHASES : ActivityOrder.SALES;
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
                mActionBar.setTitle(currentOrderType.equals(Orders.PURCHASE) ?
                        getString(R.string.purchase_orders) : getString(R.string.sale_orders));
                mActionBar.setBackground(R.mipmap.ic_bg_ab);
                break;
            case 1:
                fragment = order != null ? OrderDetailsFragment.newInstance(
                        order.getId(), order.getFacture()) : new OrderDetailsFragment();
                ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);

                mActionBar.setTitle(currentOrder.getFactureID());
                if (order.getFacture() == 1)
                    mActionBar.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                break;
        }

        ft.replace(R.id.frame_container, fragment, "fragment" + position);
        ft.commit();
    }

    /**
     * Retrieves the type of orders to be currently displayed.
     */
    private String retrieveOrdersType() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                return data.getString("orderType");
            }
        }

        return null;
    }

    @Override
    public void onAddPressed() {
        Class<?> targetActivity = Orders.PURCHASE.equals(currentOrderType) ?
                PurchasesActivity.class : SalesActivity.class;

        startActivityForResult(new Intent(this, targetActivity), REQUEST_CREATE_ORDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CREATE_ORDER:
                switch (resultCode) {
                    case ResultCodes.ORDER_CREATED:
                        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_container);
                        if (fragment instanceof OrdersListFragment)
                            ((OrdersListFragment) fragment).refresh();
                        else
                            displayFragment(0);

                        Snackbar.make(getWindow().getDecorView(),
                                R.string.order_created, Snackbar.LENGTH_LONG).show();
                        break;
                }
                break;
        }
    }

    /**
     * Fetches orders' data on behalf of the fragment.
     *
     * @param url
     * @param listener
     */
    @Override
    public void fetchOrders(String url, RequestListener listener) {
        JSONObject data = JSONUtils.bundleLocaleIDToJSON(mDatabaseAdapter.getCurrentLocaleID());
        sendHTTPRequest(url, data, Method.GET, listener);
    }

    @Override
    public void onOrderItemPressed(Order order) {
        currentOrder = order;
        displayFragment(1, order);
    }

    /**
     * Retrieves the details of the order that is currently being viewed.
     * The fragment does not need to supply any information about the order
     * because it's already stored on the activity level.
     *
     * @param requestListener to be executed as a response to the outgoing HTTP request.
     */
    @Override
    public void fetchOrderDetails(RequestListener requestListener) {
        // Bundle the request params as JSON
        JSONObject params = JSONUtils.bundleOrderToJSON(currentOrder);
        // Decide which one is the target URL
        String url = Orders.SALE.equals(currentOrderType) ?
                PhpAPI.getSaleDetails : PhpAPI.getPurchaseDetails;

        sendHTTPRequest(url, params, Method.POST, requestListener);
    }

    @Override
    public void chargeOrder(RequestListener requestListener) {
        JSONObject params = JSONUtils.bundleChargeIDToJSON(currentOrder.getId());
        String url = Orders.SALE.equals(currentOrderType) ?
                PhpAPI.chargeSaleOrder : PhpAPI.chargePurchaseOrder;
        sendHTTPRequest(url, params, Method.POST, requestListener);
    }

    @Override
    public void onOrderCharged() {
        mActionBar.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        Snackbar.make(getWindow().getDecorView(),
                R.string.order_billed, Snackbar.LENGTH_LONG).show();
    }
}
