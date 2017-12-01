package com.lidchanin.crudindiploma;

import android.os.Environment;

public interface Constants {
    interface Tessaract {
        String SLASH = "/";
        String DATA_PATH = String.valueOf(Environment.getExternalStorageDirectory());
        String TESSDATA = "tessdata";
        String RUS_TESS_SHARED = "rus";
        String ENG_TESS_SHARED = "eng";
        String ENGTRAIN = "eng.traineddata";
        String RUSTRAIN = "rus.traineddata";
    }

    interface SharedPreferences {
        String PREF_KEY_LANG_RECOGNIZE = "recognize_lang";
        String PREF_KEY_SCAN_TIMES = "scan_times";
        String PREF_KEY_THEME = "theme";
    }

    interface Bundles {
        String SHOPPING_LIST_FRAGMENT_ID = "shoppingListFragmentId";
        String ALL_PRODUCTS_FRAGMENT_ID = "allProductsFragmentId";
        String PROFIT_FRAGMENT_ID = "profitFramgentId";
        String SETTINGS_FRAGMENT_ID = "settingsFragmentId";
        String STATISTICS_FRAGMENT_ID = "statisticsFragmentId";
        String ABOUT_US_FRAGMENT_ID = "aboutUsFragmentId";
        String FRAGMENT_ID = "fragmentId";
        String SHOPPING_LIST_ID = "shoppingListId";
        String VIEWPAGER_PAGE = "shoppingListId";
    }

    interface ThemesBright {
        String THEME_RED_SOFT = "redsoft";
        String THEME_GREEN = "green";
        String THEME_BLUE = "blue";
        String THEME_PURPLE = "purple";
    }

    interface ThemesDark {
        String THEME_RED_SOFT = "redsoftdark";
        String THEME_GREEN = "greendark";
        String THEME_BLUE = "bluedark";
        String THEME_PURPLE = "purpledark";
    }

    interface ThemesMaterials {
        String THEME_RED_SOFT = "redsoftmaterial";
        String THEME_GREEN = "greenmaterial";
        String THEME_BLUE = "bluematerial";
        String THEME_PURPLE = "purplematerial";
    }

    interface Tutorial {
        String IMAGES = "tutorialImages";
        String STRINGS = "tutorialStrings";
    }
}