package com.lidchanin.crudindiploma.utils;

import android.content.Context;
import android.os.Build;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;

public class ThemeManager {
    public ThemeManager(Context context){
        final String prefOutput =new SharedPrefsManager(context).readString(Constants.SharedPreferences.PREF_KEY_THEME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            if (prefOutput.equals("blue")||prefOutput.equals("")) {
                context.setTheme(R.style.BlueGradientTheme);
            }
            if (prefOutput.equals("purple")) {
                context.setTheme(R.style.PurpleGradientTheme);
            }
            if (prefOutput.equals("turquoise")) {
                context.setTheme(R.style.TurquoiseGradientTheme);
            }
            if (prefOutput.equals("virgin")) {
                context.setTheme(R.style.VirginGradientTheme);
            }
            if (prefOutput.equals("loveAndLiberty")) {
                context.setTheme(R.style.LoveAndLibertyGradientTheme);
            }
        }
    }
}
