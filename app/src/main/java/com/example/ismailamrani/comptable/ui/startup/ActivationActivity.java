package com.example.ismailamrani.comptable.ui.startup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Activation;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.SuccessRequestListener;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.ui.ActivityTransition;
import com.example.ismailamrani.comptable.utils.http.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivationActivity extends ColoredStatusBarActivity {

    @Bind(R.id.activationField)
    EditText activationField;
    @Bind(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        ButterKnife.bind(this);
    }

    public void onActivate(View view) {
        String serial = activationField.getText().toString();
        if (!TextUtils.isEmpty(serial)) {
            mLoadingDialog.show();
            sendHTTPRequest(
                    PhpAPI.activateApp,
                    JSONUtils.bundleSerialToJSON(serial),
                    Method.POST,
                    new ActivationListener()
            );
        } else
            Toast.makeText(this, R.string.activation_code_required, Toast.LENGTH_LONG).show();
    }

    public void onCallUs(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + ((TextView) view).getText()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    /**
     * A listener on the activation procedure.
     */
    public class ActivationListener extends SuccessRequestListener {
        @Override
        public void onRequestSucceeded(final JSONObject response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ActivationActivity.this,
                            R.string.application_activated,
                            Toast.LENGTH_SHORT).show();
                    try {
                        Activation activation = new Activation(
                                response.getJSONArray("activation").getJSONObject(0));
                        mDatabaseAdapter.activateApplication(activation);

                        activityShouldFinish();
                        ActivityTransition.startActivityWithSharedElement(
                                ActivationActivity.this, LoginActivity.class,
                                imageView, "header");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onRequestFailed(int status, JSONObject response) {
            int message;

            if (status == -1)
                message = R.string.code_already_used;
            else // status == 0
                message = R.string.invalid_activation_code;

            final int finalMessage = message;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ActivationActivity.this, finalMessage,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
