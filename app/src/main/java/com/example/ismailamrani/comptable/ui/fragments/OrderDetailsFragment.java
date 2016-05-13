package com.example.ismailamrani.comptable.ui.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.OrderDetailsAdapter;
import com.example.ismailamrani.comptable.models.OrderDetail;
import com.example.ismailamrani.comptable.utils.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {

    private int currentOrderID;
    private OrderDetailsFragListener listener;

    @Bind(R.id.detailsListView)
    ListView detailsListView;

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
        ButterKnife.bind(this, view);

        currentOrderID = getCurrentOrderID();
        if (listener != null)
            listener.fetchOrderDetails(new RequestListener() {
                @Override
                public void onRequestSucceeded(JSONObject response, int status) {
                    if (status == 1) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("orderDetails");
                            List<OrderDetail> details = OrderDetail.parseSuppliers(jsonArray);

                            getActivity().runOnUiThread(() ->
                                    detailsListView.setAdapter(
                                            new OrderDetailsAdapter(getActivity(), details)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getActivity(), "Unknown error",
                                        Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onRequestFailed() {

                }
            });

        return view;
    }

    private int getCurrentOrderID() {
        Bundle data = getArguments();

        if (data != null)
            return data.getInt("orderID");

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
        void fetchOrderDetails(RequestListener requestListener);
    }
}
