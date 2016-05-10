package com.example.ismailamrani.comptable.ui.startup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivationActivity extends ColoredStatusBarActivity {

    @Bind(R.id.activationField)
    EditText activationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        ButterKnife.bind(this);
    }

    public void onActivate(View view) {
        String serial = activationField.getText().toString();
        if (!TextUtils.isEmpty(serial)) {

        }
        else {
            Toast.makeText(this, "Activation code required.", Toast.LENGTH_LONG).show();
        }
    }
}
