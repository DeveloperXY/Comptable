package com.example.ismailamrani.comptable.customitems.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.orders.purchases.ItemAdapter;
import com.example.ismailamrani.comptable.customitems.OGActionBar.OGActionBar;
import com.example.ismailamrani.comptable.models.Item;
import com.example.ismailamrani.comptable.models.Product;
import com.example.ismailamrani.comptable.models.Supplier;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mohammed Aouf ZOUAG on 03/05/2016.
 */
public class SpinnerBottomSheet extends BottomSheetDialog {

    private OkHttpClient client = new OkHttpClient();

    private Context context;
    private RecyclerView recyclerView;
    private TextView titleBar;
    private TextView emptyView;

    private ItemAdapter itemAdapter;
    private OnItemSelectedListener listener;

    private int spinnerID;

    public SpinnerBottomSheet(@NonNull Context context, int spinnerID) {
        super(context);
        this.context = context;
        this.spinnerID = spinnerID;

        View view = getLayoutInflater().inflate(R.layout.sheet, null);
        setContentView(view);
        setOnDismissListener(dialog -> this.context = null);

        setupUI();
        fetchAppropriateData();
    }

    private void setupUI() {
        setupRecyclerView();

        emptyView = (TextView) findViewById(R.id.emptyTextView);
        titleBar = (TextView) findViewById(R.id.titleBar);
    }

    private void fetchAppropriateData() {
        getFetch(spinnerID == R.id.productSpinner ? PhpAPI.getProduit :
                PhpAPI.getFournisseur, null);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.spinnerRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
    }

    void getFetch(String url, JSONObject data) {
        Request request = PhpAPI.createHTTPRequest(data, url, Method.GET);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        ((AppCompatActivity) context).runOnUiThread(() -> {
                            titleBar.setText("Waiting for network...");
                            Toast.makeText(context, "No internet connection.",
                                    Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(res);
                            int resp = obj.getInt("success");

                            ((AppCompatActivity) context).runOnUiThread(() -> {
                                if (resp == 0) {
                                    Toast.makeText(context, "Error",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    if (spinnerID == R.id.productSpinner) {
                                        titleBar.setText("Available products");
                                        try {
                                            List<Product> products = Product.parseProducts(
                                                    obj.getJSONArray("produit"));
                                            populateRecyclerView(Stream.of(products)
                                                    .map(p -> ((Item) p))
                                                    .collect(Collectors.toList()));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        titleBar.setText("Available suppliers");
                                        try {
                                            List<Supplier> suppliers = Supplier.parseSuppliers(
                                                    obj.getJSONArray("fournisseur"));
                                            populateRecyclerView(Stream.of(suppliers)
                                                    .map(s -> ((Item) s))
                                                    .collect(Collectors.toList()));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void populateRecyclerView(List<Item> items) {
        if (items.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            if (spinnerID == R.id.productSpinner) {
                titleBar.setText("Your stock is empty.");
                emptyView.setText("You need to add products to your stock first.");
            }
            else {
                titleBar.setText("You have no registered suppliers.");
                emptyView.setText("");
            }
        }
        else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (itemAdapter == null) {
            itemAdapter = new ItemAdapter(items, item -> {
                listener.onItemSelected(item);
                dismiss();
            });
            recyclerView.setAdapter(itemAdapter);
        } else
            itemAdapter.refill(items);
    }

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Item item);
    }
}
