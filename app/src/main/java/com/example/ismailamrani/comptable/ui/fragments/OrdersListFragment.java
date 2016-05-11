package com.example.ismailamrani.comptable.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Order;
import com.example.ismailamrani.comptable.ui.orders.adapters.OrdersAdapter;
import com.example.ismailamrani.comptable.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersListFragment extends Fragment {

    protected List<Order> mOrders;
    protected OrdersAdapter ordersAdapter;

    /**
     * The orders' list.
     */
    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @Bind(R.id.emptyMessageLabel)
    protected TextView emptyMessageLabel;

    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;

    /**
     * The view to be displayed in case a network error occur.
     */
    @Bind(R.id.errorLayout)
    protected RelativeLayout errorLayout;

    /**
     * The view to be displayed in case there were no orders to show.
     */
    @Bind(R.id.emptyLayout)
    protected RelativeLayout emptyView;

    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

    public OrdersListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);
        ButterKnife.bind(this, view);

        setupSwipeRefresh();
        setupRecyclerView();

        return view;
    }

    protected void setupRecyclerView() {
        mOrders = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));
    }

    protected void setupSwipeRefresh() {
//        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipeRefresh1,
                R.color.swipeRefresh2,
                R.color.swipeRefresh3,
                R.color.swipeRefresh4
        );
    }
}
