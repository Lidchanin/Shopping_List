package com.lidchanin.crudindiploma.utils;

import android.content.Context;
import android.os.Build;

import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.forlib.RecyclerViewItems;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    public static ThemeManager getInstance(Context context){
        return new ThemeManager(context);
    }

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

    public List<RecyclerViewItems> getThemes(String type){
        switch (type){
            case "Dark":return getDarkThemes();
            case "Bright":return getBrightThemes();
            case "Material":return getMaterialThemes();
        }
        return null;
    }

    private List<RecyclerViewItems> getMaterialThemes(){
        List<RecyclerViewItems> list = new ArrayList<>();
        list.add(new RecyclerViewItems("freeBright","blue"));
        return list;
    }

    private List<RecyclerViewItems> getDarkThemes(){
        List<RecyclerViewItems> list = new ArrayList<>();
        list.add(new RecyclerViewItems("freeBright","blue"));

        return list;
    }

    private List<RecyclerViewItems> getBrightThemes(){
        List<RecyclerViewItems> list = new ArrayList<>();
        list.add(new RecyclerViewItems("freeBright","blue"));
        list.add(new RecyclerViewItems("freeBright","purple"));
        list.add(new RecyclerViewItems("freeBright","turquoise"));
        list.add(new RecyclerViewItems("freeBright","virgin"));
        list.add(new RecyclerViewItems("freeBright","loveAndLiberty"));
        return list;
    }
}
