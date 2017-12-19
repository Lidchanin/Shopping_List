package com.lidchanin.shoppinglist.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    public void editString(String key,String value){
        sharedPreferencesEditor.putString(key,value).apply();
    }

    public String readString(String key) {
        if (sharedPreferences.contains(key)){
           return sharedPreferences.getString(key, null);
        }else{
            return "";
        }
    }



}
