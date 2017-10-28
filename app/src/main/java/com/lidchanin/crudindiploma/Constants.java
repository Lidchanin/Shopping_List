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
        String INSIDE_SHOPPING_LIST_FRAGMENT_ID = "insideShoppingListFragmentId";
        String FRAGMENT_ID = "fragmentId";
        String SHOPPING_LIST_ID = "shoppingListId";
        String VIEWPAGER_PAGE = "shoppingListId";
    }
}