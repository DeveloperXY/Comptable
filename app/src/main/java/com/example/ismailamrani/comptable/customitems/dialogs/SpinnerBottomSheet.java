package com.example.ismailamrani.comptable.customitems.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.spinners.ItemAdapter;
import com.example.ismailamrani.comptable.models.Product;
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
    private int spinnerID;

    public SpinnerBottomSheet(@NonNull Context context, int spinnerID) {
        super(context);
        this.context = context;
        this.spinnerID = spinnerID;

        View view = getLayoutInflater().inflate(R.layout.sheet, null);
        setContentView(view);
        setOnDismissListener(dialog -> this.context = null);

        setupRecyclerView();
        fetchAppropriateData();
    }

    private void fetchAppropriateData() {
        getFetch(spinnerID == R.id.productSpinner ? PhpAPI.getProduit :
                PhpAPI.getFournisseur, null);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.spinnerRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        recyclerView.setAdapter(new ItemAdapter(createItems(), item -> dismiss()));
    }

    void getFetch(String url, JSONObject data) {
        Request request = PhpAPI.createHTTPRequest(data, url, Method.GET);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        getOwnerActivity().runOnUiThread(() -> Toast.makeText(context,
                                call.request().toString(), Toast.LENGTH_LONG).show());
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
                                }
                                else {
                                    if (spinnerID == R.id.productSpinner) {
                                        try {
                                            List<Product> products = Product.parseProducts(
                                                    obj.getJSONArray("produit"));
                                            Log.i("RESULT", products.toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        Log.i("RESULT", "Fournisseurs: " + res);
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
