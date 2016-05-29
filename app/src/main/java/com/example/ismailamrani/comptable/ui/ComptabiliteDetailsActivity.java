package com.example.ismailamrani.comptable.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;
import com.example.ismailamrani.comptable.utils.JSONUtils;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ComptabiliteDetailsActivity extends AnimatedActivity {

    @Bind(R.id.productNumberValue)
    TextView productNumberValue;
    @Bind(R.id.articleNumberValue)
    TextView articleNumberValue;
    @Bind(R.id.suppliersNumberValue)
    TextView suppliersNumberValue;
    @Bind(R.id.clientsNumberValue)
    TextView clientsNumberValue;
    @Bind(R.id.salesNumberValue)
    TextView salesNumberValue;
    @Bind(R.id.purchasesNumberValue)
    TextView purchasesNumberValue;
    @Bind(R.id.chargesNumberValue)
    TextView chargesNumberValue;
    @Bind(R.id.purchaseOperationsNumber)
    TextView purchaseOperationsNumber;
    @Bind(R.id.saleOperationsNumber)
    TextView saleOperationsNumber;
    @Bind(R.id.totalPriceLabel)
    TextView totalPriceLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptabilite_details);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();

        fetchDetails();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Comptabilité");
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.COMPTABILITE;
    }

    private void fetchDetails() {
        JSONObject data = JSONUtils.merge(
                JSONUtils.bundleCompanyIDToJSON(mDatabaseAdapter.getUserCompanyID()),
                JSONUtils.bundleLocaleIDToJSON(mDatabaseAdapter.getCurrentLocaleID()));

        sendHTTPRequest(PhpAPI.getComptabilite, data, Method.GET,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response, int status) {
                        if (status == 1) {
                            try {
                                JSONObject details = response.getJSONArray("comptabilite")
                                        .getJSONObject(0);

                                showData(details);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onRequestFailed() {
                        runOnUiThread(() -> Toast.makeText(ComptabiliteDetailsActivity.this,
                                "Network error.", Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void showData(JSONObject details) throws JSONException {
        productNumberValue.setText(details.getString("NbrMarque"));
        articleNumberValue.setText(details.getString("QteProduit"));
        suppliersNumberValue.setText(details.getString("NbrFournisseur"));
        clientsNumberValue.setText(details.getString("NbrClient"));
        salesNumberValue.setText(details.getString("TotalVente") + " DH");
        purchasesNumberValue.setText(details.getString("TotalAchat") + " DH");
        chargesNumberValue.setText(details.getString("TotalCharge") + " DH");
        purchaseOperationsNumber.setText(details.getString("NbrOperationAchat"));
        saleOperationsNumber.setText(details.getString("NbrOperationVente"));
        totalPriceLabel.setText(details.getString("Profit") + " DH");
    }
}
