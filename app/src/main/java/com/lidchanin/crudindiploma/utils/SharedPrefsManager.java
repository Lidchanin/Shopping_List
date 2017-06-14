package com.lidchanin.crudindiploma.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.lidchanin.crudindiploma.Constants;

/**
 * Created by Alexander Destroyed on 21.05.2017.
 */

public class SharedPrefsManager {
    private Context context;
    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor sharedPreferencesEditor;
    public SharedPrefsManager(Context context){
        this.context=context;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesEditor= sharedPreferences.edit();
    }

}
