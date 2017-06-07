package com.lidchanin.crudindiploma.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Alexander Destroyed on 21.05.2017.
 */

public class SharedPrefsManager {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    public SharedPrefsManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public void editBoolean(String key, boolean value) {
        sharedPreferencesEditor.putBoolean(key, value).commit();
    }

    public boolean readBoolean(String key) {
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getBoolean(key, true);
        } else {
            editBoolean(key, false);
            return false;
        }
    }

}