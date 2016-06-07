package com.example.ismailamrani.comptable.ui.dialogs;

import android.content.Context;

import com.example.ismailamrani.comptable.models.Client;
import com.example.ismailamrani.comptable.ui.dialogs.base.ItemDialog;

/**
 * Created by Mohammed Aouf ZOUAG on 01/06/2016.
 */
public class ClientDialog extends ItemDialog<Client> {

    public ClientDialog(Context context, Client client) {
        super(context, client);
    }

    @Override
    protected void bind(Client item) {
        titleLabel.setText(item.getNomPrenom());
        emailLabel.setText(item.getEmail());
        addressLabel.setText(item.getAdresse());
        phoneLabel.setText(item.getTel());
    }

    @Override
    protected String getItemPhoneNumber() {
        return item.getTel();
    }
}
