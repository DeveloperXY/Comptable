package com.example.ismailamrani.comptable.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.ui.fragments.OrderDetailsFragment;
import com.example.ismailamrani.comptable.ui.fragments.OrdersListFragment;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.utils.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrdersActivity extends ColoredStatusBarActivity
        implements OrdersListFragment.OrderListFragListener,
        OrderDetailsFragment.OrderDetailsFragListener {

    public static final String PURCHASE_ORDERS = "Commandes achats";
    public static final String SALE_ORDERS = "Commandes ventes";

    public static final int REQUEST_CREATE_ORDER = 1;

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
                mActionBar.setTitle(currentOrderType.equals("PURCHASE") ?
                        PURCHASE_ORDERS : SALE_ORDERS);
                mActionBar.setBackground(R.mipmap.ic_bg_ab);
                break;
            case 1:
                fragment = order != null ? OrderDetailsFragment.newInstance(
                        order.getId(), order.getFacture()) : new OrderDetailsFragment();
                ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);

                mActionBar.setTitle(currentOrder.getFactureID());
                if (order.getFacture() == 1)
                    mActionBar.setBackgroundColor("#2E7D32");

                break;
        }

        ft.replace(R.id.frame_container, fragment, "fragment" + position);
        ft.commit();
    }

    protected void setupActionBar() {
        mActionBar.setActionBarListener(this);
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
        Class<?> targetActivity = "PURCHASE".equals(currentOrderType) ?
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

                        Snackbar.make(getWindow().getDecorView(),
                                "Commande créée avec succès.", Snackbar.LENGTH_LONG).show();
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
        sendHTTPRequest(url, null, Method.GET, listener);
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
        String url = "SALE".equals(currentOrderType) ?
                PhpAPI.getSaleDetails : PhpAPI.getPurchaseDetails;

        sendHTTPRequest(url, params, Method.POST, requestListener);
    }
}
