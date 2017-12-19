package com.lidchanin.shoppinglist.utils;

import android.content.Context;
import android.os.Build;

import com.lidchanin.shoppinglist.Constants;
import com.lidchanin.shoppinglist.R;
import com.lidchanin.shoppinglist.models.ThemeViewModels;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    private Context context;

    public ThemeManager(Context context) {
        this.context = context;

        final String prefOutput = new SharedPrefsManager(context)
                .readString(Constants.SharedPreferences.PREF_KEY_THEME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            if (prefOutput.equals(Constants.ThemesBright.THEME_BLUE)) {
                context.setTheme(R.style.BlueTheme);
            }
            if (prefOutput.equals(Constants.ThemesBright.THEME_PURPLE)) {
                context.setTheme(R.style.PurpleTheme);
            }
            if (prefOutput.equals(Constants.ThemesBright.THEME_GREEN)) {
                context.setTheme(R.style.GreenTheme);
            }
            if (prefOutput.equals(Constants.ThemesBright.THEME_RED_SOFT) ) {
                context.setTheme(R.style.RedsoftTheme);
            }
            if (prefOutput.equals(Constants.ThemesDark.THEME_BLUE)) {
                context.setTheme(R.style.BlueDarkTheme);
            }
            if (prefOutput.equals(Constants.ThemesDark.THEME_PURPLE)) {
                context.setTheme(R.style.PurpleDarkTheme);
            }
            if (prefOutput.equals(Constants.ThemesDark.THEME_GREEN)) {
                context.setTheme(R.style.GreenDarkTheme);
            }
            if (prefOutput.equals(Constants.ThemesDark.THEME_RED_SOFT)) {
                context.setTheme(R.style.RedsoftDarkTheme);
            }
            if (prefOutput.equals(Constants.ThemesMaterials.THEME_BLUE)|| prefOutput.equals("")) {
                context.setTheme(R.style.BlueMaterialTheme);
            }
            if (prefOutput.equals(Constants.ThemesMaterials.THEME_PURPLE)) {
                context.setTheme(R.style.PurpleMaterialTheme);
            }
            if (prefOutput.equals(Constants.ThemesMaterials.THEME_GREEN)) {
                context.setTheme(R.style.GreenMaterialTheme);
            }
            if (prefOutput.equals(Constants.ThemesMaterials.THEME_RED_SOFT)) {
                context.setTheme(R.style.RedsoftMaterialTheme);
            }
            if (prefOutput.equals("loveAndLiberty")) {
                context.setTheme(R.style.LoveAndLibertyGradientTheme);
            }
        }
    }

    public static ThemeManager getInstance(Context context) {
        return new ThemeManager(context);
    }

    public List<ThemeViewModels> getThemes(String type) {
        if (type.equals(context.getString(R.string.dark))) {
            return getDarkThemes();
        } else if (type.equals(context.getString(R.string.bright))) {
            return getBrightThemes();
        } else if (type.equals(context.getString(R.string.material))) {
            return getMaterialThemes();
        } else {
            return new ArrayList<>();
        }

        /*switch (type) {
            case "Dark":
                return getDarkThemes();
            case "Bright":
                return getBrightThemes();
            case "Material":
                return getMaterialThemes();
        }
        return null;*/
    }

    private List<ThemeViewModels> getMaterialThemes() {
        List<ThemeViewModels> list = new ArrayList<>();
        list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.blue),
                        Constants.ThemesMaterials.THEME_BLUE,
                        R.drawable.material_blue
                )
        );
        /*list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.purple),
                        Constants.ThemesMaterials.THEME_PURPLE,
                        R.drawable.material_purple
                )
        );
        list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.green),
                        Constants.ThemesMaterials.THEME_GREEN,
                        R.drawable.material_green
                )
        );
        list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.red_soft),
                        Constants.ThemesMaterials.THEME_RED_SOFT,
                        R.drawable.material_redsoft
                )
        );*/
        return list;
    }

    private List<ThemeViewModels> getDarkThemes() {
        List<ThemeViewModels> list = new ArrayList<>();
        list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.blue),
                        Constants.ThemesDark.THEME_BLUE,
                        R.drawable.dark_blue
                )
        );
        /*list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.purple),
                        Constants.ThemesDark.THEME_PURPLE,
                        R.drawable.dark_purple
                )
        );
        list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.green),
                        Constants.ThemesDark.THEME_GREEN,
                        R.drawable.dark_green
                )
        );
        list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.red_soft),
                        Constants.ThemesDark.THEME_RED_SOFT,
                        R.drawable.dark_redsoft
                )
        );*/
        return list;
    }

    private List<ThemeViewModels> getBrightThemes() {
        List<ThemeViewModels> list = new ArrayList<>();
        list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.blue),
                        Constants.ThemesBright.THEME_BLUE,
                        R.drawable.blue
                )
        );
        /*list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.purple),
                        Constants.ThemesBright.THEME_PURPLE,
                        R.drawable.purple
                )
        );
        list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.green),
                        Constants.ThemesBright.THEME_GREEN,
                        R.drawable.green
                )
        );
        list.add(new ThemeViewModels(
                        context.getString(R.string.free),
                        context.getString(R.string.red_soft),
                        Constants.ThemesBright.THEME_RED_SOFT,
                        R.drawable.redsoft
                )
        );*/
        return list;
    }
}
