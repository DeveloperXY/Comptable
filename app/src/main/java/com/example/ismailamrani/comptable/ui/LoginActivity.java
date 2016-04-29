package com.example.ismailamrani.comptable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.Models.User;
import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.ServiceWeb.PhpAPI;
import com.example.ismailamrani.comptable.UsedMethodes.CalculateScreenSize;
import com.example.ismailamrani.comptable.utils.DialogUtil;
import com.example.ismailamrani.comptable.utils.Method;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient();

    LinearLayout Valider;
    EditText nom, motdepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CalculateScreenSize().CalculateScreenSize(this);
        setContentView(R.layout.activity_splash);

        Valider = (LinearLayout) findViewById(R.id.Valider);
        nom = (EditText) findViewById(R.id.username);
        motdepass = (EditText) findViewById(R.id.mp);

        Valider.setOnClickListener(v -> {
            String username = nom.getText().toString();
            String password = motdepass.getText().toString();
            User user = validateUserCredentials(username, password);

            if (user != null) {
                // Send the login POST request.
                try {
                    postLogin(PhpAPI.login, user.toJSON());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param username
     * @param password
     * @return a new User object if the username & password are valid, or null otherwise.
     */
    public User validateUserCredentials(String username, String password) {
        String dialogTitle;
        String dialogMessage;

        boolean usernameStatus = username.length() != 0;
        boolean passwordStatus = password.length() != 0;

        if (usernameStatus && passwordStatus)
            return new User.Builder()
                    .username(username)
                    .password(password)
                    .createUser();

        if (!usernameStatus) {
            // Invalid user name.
            dialogTitle = "Invalid user name.";
            dialogMessage = "You need to specify your user name.";
        } else {
            // Invalid password.
            dialogTitle = "Invalid password.";
            dialogMessage = "You need to specify your password.";
        }

        // Something went wrong: show the error dialog.
        DialogUtil.showDialog(this, dialogTitle, dialogMessage, "OK", null);

        return null;
    }

    /**
     * @param url             destination URL
     * @param userCredentials the user's informations
     * @throws IOException
     */
    void postLogin(String url, JSONObject userCredentials) throws IOException {
        Request request = PhpAPI.createHTTPRequest(userCredentials, url, Method.POST);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this,
                                call.request().toString(), Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String res = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(res);
                            int resp = obj.getInt("success");

                            runOnUiThread(() -> {
                                if (resp == 1)
                                    startActivity(new Intent(
                                            LoginActivity.this, HomeActivity.class));
                                else if (resp == 0)
                                    Toast.makeText(LoginActivity.this,
                                            "Unregistered user name.",
                                            Toast.LENGTH_LONG).show();
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
