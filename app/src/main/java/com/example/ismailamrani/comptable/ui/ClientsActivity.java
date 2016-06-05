package com.example.ismailamrani.comptable.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.adapters.ClientAdapter;
import com.example.ismailamrani.comptable.models.Client;
import com.example.ismailamrani.comptable.ui.base.RefreshableActivity;
import com.example.ismailamrani.comptable.ui.dialogs.ClientDialog;
import com.example.ismailamrani.comptable.utils.decorations.GridSpacingItemDecoration;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.parsing.ListComparison;
import com.example.ismailamrani.comptable.utils.ui.ActivityTransition;
import com.example.ismailamrani.comptable.utils.ui.DialogUtil;
import com.example.ismailamrani.comptable.utils.ui.ResultCodes;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Redouane on 31/03/2016.
 * Altered by Mohammed Aouf ZOUAG on 01/06/2016.
 */
public class ClientsActivity extends RefreshableActivity {

    private static final int REQUEST_ADD_CLIENT = 1;
    private static final int REQUEST_UPDATE_CLIENT = 2;

    private List<Client> mClients;
    private ClientAdapter mClientAdapter;
    private ClientDialog mClientDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        ButterKnife.bind(this);

        setupActionBar();
        setupRevealTransition();
        setupRecyclerView();
        setupSwipeRefresh();

        refresh();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();

        mActionBar.setTitle(getString(R.string.clients));
    }

    @Override
    public void onAddPressed() {
        ActivityTransition.startActivityForResultWithSharedElement(
                this, new Intent(this, AlterClientActivity.class), actionbarImage,
                "menuAnim", REQUEST_ADD_CLIENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_CLIENT:
            case REQUEST_UPDATE_CLIENT:
                switch (resultCode) {
                    case ResultCodes.CLIENT_CREATED:
                    case ResultCodes.CLIENT_UPDATED:
                        refresh();
                        break;
                }
                break;
        }
    }

    @Override
    public ActivityOrder getActivity() {
        return ActivityOrder.CLIENTS;
    }

    private void fetchClients() {
        JSONObject data = JSONUtils.bundleCompanyIDToJSON(
                mDatabaseAdapter.getUserCompanyID());
        sendHTTPRequest(PhpAPI.getClient, data, Method.POST,
                new RequestListener() {
                    @Override
                    public void onRequestSucceeded(JSONObject response) {
                        try {
                            JSONArray productList = response.getJSONArray("client");
                            List<Client> clients = Client.parseClients(productList);

                            if (ListComparison.areEqual(mClients, clients))
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        handleDataChange();
                                    }
                                });
                            else {
                                mClients = clients;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        handleDataChange();
                                        populateRecyclerView();
                                    }
                                });
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    populateRecyclerView();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    dataRecyclerView.requestLayout();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void handleDataChange() {
                        toggleRecyclerviewState();
                        progressBar.setVisibility(View.INVISIBLE);
                        stopSwipeRefresh();
                    }

                    @Override
                    public void onRequestFailed(int status, JSONObject response) {

                    }

                    @Override
                    public void onNetworkError() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleRequestError();
                            }
                        });
                    }
                });
    }

    @Override
    protected void setupRecyclerView() {
        mClients = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        dataRecyclerView.setLayoutManager(layoutManager);
        dataRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        dataRecyclerView.setItemAnimator(new DefaultItemAnimator());

        emptyMessageLabel.setText(R.string.no_clients_to_show);
    }

    @Override
    protected void refresh() {
        fetchClients();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * Toggles the visibility of the RecyclerView & the empty view associated with it.
     */
    private void toggleRecyclerviewState() {
        emptyLayout.setVisibility(mClients.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        dataRecyclerView.setVisibility(mClients.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
    }

    private void populateRecyclerView() {
        if (mClientAdapter == null) {
            mClientAdapter = new ClientAdapter(this, mClients);
            mClientAdapter.setClientListener(new ClientAdapter.ClientListener() {
                @Override
                public void onClientSelected(Client client) {
                    mClientDialog = new ClientDialog(ClientsActivity.this, client);
                    mClientDialog.show();
                }

                @Override
                public void onEditClient(Client client, ImageView itemImage) {
                    Intent i = new Intent(ClientsActivity.this, AlterClientActivity.class);
                    i.putExtra("client", client);
                    i.putExtra("isUpdating", true);
                    ActivityTransition.startActivityForResultWithSharedElement(
                            ClientsActivity.this, i, itemImage, "menuAnim",
                            REQUEST_UPDATE_CLIENT);
                }

                @Override
                public void onDeleteClient(String clientID) {
                    deleteClient(clientID);
                }
            });
            dataRecyclerView.setAdapter(mClientAdapter);
        } else
            mClientAdapter.animateTo(mClients);
    }

    private void deleteClient(final String clientID) {
        DialogUtil.showMutliDialog(
                ClientsActivity.this,
                getString(R.string.remove_client),
                getString(R.string.question_remove_this_client),
                getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendHTTPRequest(
                                PhpAPI.removeClient,
                                JSONUtils.bundleIDToJSON(clientID),
                                Method.POST,
                                new DeleteClientListener());
                    }
                },
                getString(R.string.no), null);
    }

    class DeleteClientListener extends SuccessRequestListener {

        @Override
        public void onRequestSucceeded(JSONObject response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ClientsActivity.this, R.string.client_removed,
                            Toast.LENGTH_LONG).show();
                    refresh();
                }
            });
        }
    }
}
