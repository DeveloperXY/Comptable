package com.example.ismailamrani.comptable.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.User;
import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.DialogUtil;
import com.example.ismailamrani.comptable.utils.Method;
import com.example.ismailamrani.comptable.utils.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class LoginActivity extends ColoredStatusBarActivity {
    private DatabaseAdapter databaseAdapter;

    LinearLayout Valider;
    EditText nom, motdepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        databaseAdapter = DatabaseAdapter.getInstance(this);

        Valider = (LinearLayout) findViewById(R.id.Valider);
        nom = (EditText) findViewById(R.id.username);
        motdepass = (EditText) findViewById(R.id.mp);

        Valider.setOnClickListener(v -> {
            String username = nom.getText().toString();
            String password = motdepass.getText().toString();
            User user = validateUserCredentials(username, password);

            if (user != null) {
                // Send the login POST request.
                sendHTTPRequest(PhpAPI.login, user.toJSON(), Method.POST,
                        new RequestListener() {
                            @Override
                            public void onRequestSucceeded(JSONObject response, int status) {
                                if (status == 1) {
                                    try {
                                        // Retrieve the logged in user's infos from
                                        // the response
                                        JSONObject loggedInUser = response.getJSONArray("user")
                                                .getJSONObject(0);
                                        // Save user to local disk
                                        saveUserToInternalDatabase(loggedInUser);
                                        // Move to main menu
                                        Intent intent = new Intent(LoginActivity.this,
                                                HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (status == 0)
                                    Toast.makeText(LoginActivity.this,
                                            "Unregistered user name.",
                                            Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed() {
                                Toast.makeText(LoginActivity.this,
                                        "Error while logging in.", Toast.LENGTH_LONG).show();
                            }
                        });
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
     * Saves the "has just logged in" user into the internal SQLite database.
     *
     * @param jsonUser to be saved.
     */
    private void saveUserToInternalDatabase(JSONObject jsonUser) {
        databaseAdapter.saveLoggedInUser(new User(jsonUser));
    }
}
