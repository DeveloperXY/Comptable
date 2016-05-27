package com.example.ismailamrani.comptable.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.ismailamrani.comptable.sqlite.DatabaseAdapter;

/**
 * Created by Mohammed Aouf ZOUAG on 27/05/2016.
 */
public class Preferences {

    private static final String CURRENT_LOCALE_ID = "currentLocaleID";

    private DatabaseAdapter mDatabaseAdapter;
    private SharedPreferences mSharedPreferences;

    public Preferences(Context context) {
        mDatabaseAdapter = DatabaseAdapter.getInstance(context);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setCurrentLocaleID(int id) {
        mSharedPreferences.edit()
                .putInt(CURRENT_LOCALE_ID, id)
                .apply();
    }

    public int getCurrentLocaleID() {
        return mSharedPreferences.getInt(CURRENT_LOCALE_ID, -1);
    }
}
