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
    private String lang;
    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor sharedPreferencesEditor;
    public SharedPrefsManager(Context context, String lang){
        this.context=context;
        this.lang = lang;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesEditor= sharedPreferences.edit();
        langHelper();
    }
    private void langHelper() {
                    if(sharedPreferences.contains(Constants.SharedPreferences.PREF_KEY_LANG_RECOGNIZE))
    {
        if (sharedPreferences.getString(Constants.SharedPreferences.PREF_KEY_LANG_RECOGNIZE, "").equals(lang)) {
            Toast.makeText(context, "It's also exists!", Toast.LENGTH_SHORT).show();
        } else {
            sharedPreferencesEditor.putString(Constants.SharedPreferences.PREF_KEY_LANG_RECOGNIZE, lang).apply();
        }
    }
}
}
