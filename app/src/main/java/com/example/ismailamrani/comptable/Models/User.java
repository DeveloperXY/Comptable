package com.example.ismailamrani.comptable.Models;

import com.example.ismailamrani.comptable.utils.DialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohammed Aouf ZOUAG on 27/04/2016.
 *
 * Represents a user in possession of a user name & a password.
 */
public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * @return a JSON object representing this user object.
     */
    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("Username", username);
            object.put("Password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
