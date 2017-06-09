package com.lidchanin.crudindiploma.utils;

public class MathUtils {

    public static Double max(Double... vals) {
        Double ret = null;
        for (Double val : vals) {
            if (ret == null || (val != null && val > ret)) {
                ret = val;
            }
        }
        return ret;
    }

}

