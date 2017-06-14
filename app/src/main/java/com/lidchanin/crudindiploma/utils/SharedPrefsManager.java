package com.lidchanin.crudindiploma.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.lidchanin.crudindiploma.Constants;

public class SharedPrefsManager {
    private Context context;
    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor sharedPreferencesEditor;
    public SharedPrefsManager(Context context){
        this.context=context;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesEditor= sharedPreferences.edit();
    }

    public void editString(String key,String value){
        sharedPreferencesEditor.putString(key,value).apply();
    }

    String readString(String key) {
        if (sharedPreferences.contains(key)){
           return sharedPreferences.getString(key, null);
        }else{
            return "blue";
        }
    }

}
