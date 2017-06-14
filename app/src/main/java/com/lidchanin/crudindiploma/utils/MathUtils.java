package com.lidchanin.crudindiploma.utils;

import java.util.Map;

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
    public static int min(Map<Integer,Double> map) {
        Double ret = null;
        int minKey=0;
        for(int i=0;i<=map.size();i++){

            if (ret == null || (map.get(i) != null && map.get(i) < ret)) {
                ret = map.get(i);
                minKey=i;
            }

        }
        return minKey;
    }

}

