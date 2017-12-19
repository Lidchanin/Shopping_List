package com.lidchanin.shoppinglist.utils;

import com.lidchanin.shoppinglist.models.ProfitItems;

import java.util.List;

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
    public static int min(List<ProfitItems> list) {
        Double ret = null;
        int minKey=0;
        for(int i=0;i<list.size();i++){
                if (ret == null || (list.get(i) != null && list.get(i).getSum() < ret)) {
                    ret = list.get(i).getSum();
                    minKey = i;
            }
        }
        return minKey;
    }

}

