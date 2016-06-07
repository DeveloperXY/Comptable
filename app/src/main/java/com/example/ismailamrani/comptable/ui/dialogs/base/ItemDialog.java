package com.example.ismailamrani.comptable.ui.dialogs.base;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.utils.ui.CalculateScreenSize;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohammed Aouf ZOUAG on 04/06/2016.
 *
 * A custom dialog to display the details of the selected T element.
 */
public abstract class ItemDialog<T> extends Dialog {

    @Bind(R.id.supplierTitleLabel)
    protected TextView titleLabel;
    @Bind(R.id.supplierEmailLabel)
    protected TextView emailLabel;
    @Bind(R.id.bottomAddressLabel)
    protected TextView addressLabel;
    @Bind(R.id.supplierPhoneLabel)
    protected TextView phoneLabel;

    protected Context mContext;
    /**
     * The item that is currently being viewed.
     */
    protected T item;

    public ItemDialog(Context context, T item) {
        super(context);
        mContext = context;
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.item_dialog);
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
        bind(item);
    }

    @OnClick(R.id.callButton)
    protected final void onClick() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + getItemPhoneNumber()));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mContext.startActivity(intent);
        dismiss();
    }

    /**
     * @param item whose info are to be displayed.
     */
    protected abstract void bind(T item);

    /**
     * @return the phone number of the currently viewed item.
     */
    protected abstract String getItemPhoneNumber();
}
