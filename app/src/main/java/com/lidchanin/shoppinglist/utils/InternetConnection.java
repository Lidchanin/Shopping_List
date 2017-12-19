package com.lidchanin.shoppinglist.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Alexander Destroyed on 29.05.2017.
 */

public class InternetConnection {

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
