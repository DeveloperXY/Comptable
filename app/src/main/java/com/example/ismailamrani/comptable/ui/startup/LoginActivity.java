package com.example.ismailamrani.comptable.ui.startup;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Local;
import com.example.ismailamrani.comptable.models.User;
import com.example.ismailamrani.comptable.adapters.DatabaseAdapter;
import com.example.ismailamrani.comptable.ui.base.ColoredStatusBarActivity;
import com.example.ismailamrani.comptable.utils.ui.ActivityTransition;
import com.example.ismailamrani.comptable.utils.ui.DialogUtil;
import com.example.ismailamrani.comptable.utils.parsing.JSONUtils;
import com.example.ismailamrani.comptable.utils.http.Method;
import com.example.ismailamrani.comptable.utils.http.RequestListener;
import com.example.ismailamrani.comptable.webservice.PhpAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends ColoredStatusBarActivity {
    private DatabaseAdapter databaseAdapter;

    @Bind(R.id.Valider)
    LinearLayout Valider;
    @Bind(R.id.username)
    EditText nom;
    @Bind(R.id.mp)
    EditText motdepass;
    @Bind(R.id.logoImage)
    ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        databaseAdapter = DatabaseAdapter.getInstance(this);

        Valider.setOnClickListener(v -> {
            String username = nom.getText().toString();
            String password = motdepass.getText().toString();
            User user = validateUserCredentials(username, password);
            int currentCompanyID = databaseAdapter.getUserCompanyID();

            if (user != null) {
                JSONObject params = user.toJSON();
                try {
                    params.put("companyID", currentCompanyID);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("ERROR", "Unable to retrieve company ID.");
                }

                // Send the login POST request.
                sendHTTPRequest(PhpAPI.login, params, Method.POST,
                        new RequestListener() {
                            @Override
                            public void onRequestSucceeded(JSONObject response, int status) {
                                if (status == 1) {
                                    try {
                                        // Retrieve the logged in user's infos from
                                        // the response
                                        JSONObject loggedInUser = response.getJSONArray("user")
                                                .getJSONObject(0);
                                        JSONArray locales = response.getJSONArray("locals");
                                        JSONObject local = locales.getJSONObject(0);

                                        loggedInUser = JSONUtils.merge(loggedInUser, local);
                                        // Save user to local disk
                                        saveUserToInternalDatabase(loggedInUser);
                                        saveLocalesToInternalDatabase(locales);

                                        // Move to main menu
                                        activityShouldFinish();
                                        runOnUiThread(() ->
                                                ActivityTransition.startActivityWithSharedElement(
                                                        LoginActivity.this, HomeActivity.class,
                                                        logoImage, "header"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if (status == 0)
                                    runOnUiThread(() -> Toast.makeText(LoginActivity.this,
                                            "Unregistered user name.",
                                            Toast.LENGTH_LONG).show());
                            }

                            @Override
                            public void onRequestFailed() {
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this,
                                        "Error while logging in.", Toast.LENGTH_LONG).show());
                            }
                        });
            }
        });
    }

    private void saveLocalesToInternalDatabase(JSONArray locales) {
        List<Local> localList = new ArrayList<>();
        for (int i = 0; i < locales.length(); i++) {
            try {
                localList.add(new Local(locales.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        databaseAdapter.saveLocales(localList);
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
