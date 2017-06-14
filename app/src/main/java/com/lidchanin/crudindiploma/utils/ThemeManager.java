package com.lidchanin.crudindiploma.utils;

import android.content.Context;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;

public class ThemeManager {
    public ThemeManager(Context context){
        final String prefOutput =new SharedPrefsManager(context).readString(Constants.SharedPreferences.PREF_KEY_THEME);
        if(prefOutput.equals("blue")){
            context.setTheme(R.style.BlueGradientTheme);
        }
        if(prefOutput.equals("purple")){
            context.setTheme(R.style.PurpleGradientTheme);
        }
    }
}
