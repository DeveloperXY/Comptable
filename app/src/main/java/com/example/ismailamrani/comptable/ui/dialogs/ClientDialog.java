package com.example.ismailamrani.comptable.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Client;
import com.example.ismailamrani.comptable.utils.ui.CalculateScreenSize;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 01/06/2016.
 */
public class ClientDialog extends Dialog {

    @Bind(R.id.supplierTitleLabel)
    TextView clientTitle;
    @Bind(R.id.supplierEmailLabel)
    TextView clientEmail;
    @Bind(R.id.bottomAddressLabel)
    TextView clientAddress;
    @Bind(R.id.supplierPhoneLabel)
    TextView clientPhone;
    @Bind(R.id.supplierLogo)
    ImageView supplierLogo;

    private Context mContext;
    private Client client;

    public ClientDialog(Context context, Client client) {
        super(context);
        mContext = context;
        this.client = client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.client_dialog);
        ButterKnife.bind(this);

        setupWindow();
        initializeUI();
    }

    private void setupWindow() {
        Point point = new Point();
        new CalculateScreenSize().calculateScreenSize(mContext, point);
        getWindow().setLayout((int) (point.x - point.x * 0.1), (int) (point.y - point.y * 0.3));
    }

    private void initializeUI() {
        clientTitle.setText(client.getNomPrenom());
        clientEmail.setText(client.getEmail());
        clientAddress.setText(client.getAdresse());
        clientPhone.setText(client.getTel());
    }
}
