package com.example.ismailamrani.comptable.ui.orders.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.utils.RequestListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {

    private int currentOrderID;
    private OrderDetailsFragListener listener;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    public static OrderDetailsFragment newInstance(int orderID) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();

        args.putInt("orderID", orderID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        currentOrderID = getCurrentOrderID();

        return view;
    }

    private int getCurrentOrderID() {
        Bundle data = getArguments();

        if (data != null)
            data.getInt("orderID");

        throw new IllegalStateException("You need to pass an order ID to the fragment.");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OrderDetailsFragListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OrderDetailsFragListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OrderDetailsFragListener {
        void fetchOrderDetails(String url, RequestListener requestListener);
    }
}
