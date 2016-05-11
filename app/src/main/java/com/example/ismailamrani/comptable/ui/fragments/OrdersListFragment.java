package com.example.ismailamrani.comptable.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ismailamrani.comptable.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersListFragment extends Fragment {


    public OrdersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_list, container, false);
    }

}
