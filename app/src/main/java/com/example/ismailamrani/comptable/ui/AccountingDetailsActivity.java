package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.AnimatedActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountingDetailsActivity extends AnimatedActivity {

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
    @Bind(R.id.plusButton)
    Button plusButton;

    /**
     * This variable holds the ID of the locale whose details will be fetched & displayed.
     * It could be the ID of the current locale if the current user is a regular employee,
     * or it could be the ID of the selected locale from the previously displayed list of locales
     * if the current user is a superior.
     */
    int currentLocaleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptabilite_details);
        ButterKnife.bind(this);

        mLoadingDialog.show();

        if (currentUserType.startsWith("e"))
            plusButton.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent != null)
            currentLocaleID = intent.getIntExtra("localeID", mDatabaseAdapter.getCurrentLocaleID());

        setupActionBar();
        setupRevealTransition();

        fetchDetails();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle("Comptabilité");
        mActionBar.disableAddButton();
    }

    public void onPlusPressed(View view) {
        onBackPressed();
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.COMPTABILITE;
    }

    private void fetchDetails() {
        JSONObject data = JSONUtils.merge(
                JSONUtils.bundleCompanyIDToJSON(mDatabaseAdapter.getUserCompanyID()),
                JSONUtils.bundleLocaleIDToJSON(currentLocaleID));

        sendHTTPRequest(PhpAPI.getComptabilite, data, Method.GET,
                new SuccessRequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        try {
                            final JSONObject details = response.getJSONArray("comptabilite")
                                    .getJSONObject(0);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        showData(details);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
