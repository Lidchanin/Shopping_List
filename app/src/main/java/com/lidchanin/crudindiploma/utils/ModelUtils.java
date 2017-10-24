package com.lidchanin.crudindiploma.utils;

import android.util.Log;

import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.ShoppingList;
import com.lidchanin.crudindiploma.database.Statistic;
import com.lidchanin.crudindiploma.database.UsedProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Lidchanin
 */
public class ModelUtils {

    private static final String TAG = ModelUtils.class.getSimpleName();


    /**
     * The method <b>isProductExists</b> checks is {@link Product} exists in list or not.
     *
     * @param products    all {@link Product}s in list.
     * @param productName needed {@link Product} name.
     * @return <i>true</i> - if {@link Product} exists <br> <i>false</i> - if not exists.
     */
    public static boolean isProductExists(final List<Product> products,
                                          final String productName) {
        for (Product p : products) {
            if (p.getName().equals(productName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method <b>isUsedProductExists</b> checks is {@link UsedProduct} exists in list or
     * not.
     *
     * @param usedProducts   all {@link UsedProduct}s in list.
     * @param shoppingListId needed {@link ShoppingList} id.
     * @param productId      needed {@link Product} id.
     * @return <i>true</i> - if {@link UsedProduct} exists <br> <i>false</i> - if not exists.
     */
    public static boolean isUsedProductExists(final List<UsedProduct> usedProducts,
                                              final long shoppingListId,
                                              final long productId) {
        for (UsedProduct usedProduct : usedProducts) {
            if (usedProduct != null && usedProduct.getShoppingListId() == shoppingListId
                    && usedProduct.getProductId().equals(productId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method <b>calculateEstimatedAmount</b> calculates of estimated amounts all costs
     * {@link UsedProduct}s in {@link ShoppingList}.
     *
     * @param usedProducts all {@link UsedProduct}s in {@link ShoppingList}.
     * @return estimated amount of all {@link UsedProduct}s in {@link ShoppingList}.
     */
    public static double calculateEstimatedAmount(List<UsedProduct> usedProducts) {
        double estimatedAmount = 0;
        for (UsedProduct up : usedProducts) {
            estimatedAmount += up.getQuantity() * up.getProduct().getCost();
        }
        return estimatedAmount;
    }

    /**
     * The method <b>getMonthsCount</b> counts the number of months in list.
     *
     * @param statistics all {@link Statistic}s in list.
     * @return the number of different mounts in list.
     */
    public static int getMonthsCount(List<Statistic> statistics) {
        if (statistics.size() > 0) {
            int monthCount = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("MMM yy", Locale.getDefault());
            String month = sdf.format(statistics.get(0).getDate());
            for (Statistic s : statistics) {
                if (!sdf.format(s.getDate()).equals(month)) {
                    month = sdf.format(s.getDate());
                    monthCount++;
                }
            }
            return monthCount;
        } else {
            return 0;
        }
    }

    /**
     * The method <b>divideStatisticsByMonths</b> divides the list with {@link Statistic}s into
     * the categories. "One" date - "many" statistics, which corresponds by date.
     *
     * @param initialStatistics all started {@link Statistic}s in list.
     * @return divided list with statistics.
     */
    public static List<List<Statistic>> divideStatisticsByMonths(List<Statistic> initialStatistics) {
        List<List<Statistic>> sortedStatistics = new ArrayList<>();
//        Log.d(TAG, "_______________________________");
        if (initialStatistics != null && initialStatistics.size() > 0) {
            String month = convertLongDateToString(initialStatistics.get(0).getDate());
//            Log.d(TAG, "divideStatisticsByMonths: \ncurrent month : " + month);
            List<Statistic> statisticsByOneMonth = new ArrayList<>();
            for (Statistic s : initialStatistics) {
                if (convertLongDateToString(s.getDate()).equals(month)) {
                    statisticsByOneMonth.add(s);
                } else {
                    sortedStatistics.add(statisticsByOneMonth);
                    statisticsByOneMonth = new ArrayList<>();
                    statisticsByOneMonth.add(s);
                    month = convertLongDateToString(s.getDate());
                }
//                Log.d(TAG, "divideStatisticsByMonths: \ncurrent month : " + month);
            }
            sortedStatistics.add(statisticsByOneMonth);
        }
        return sortedStatistics;
    }

    /**
     * The method <b>convertLongDateToString</b> converts <i>System.currentTimeMillis()</i> to date
     * only with year and month.
     *
     * @param date the date in milliseconds.
     * @return string date with year and month.
     */
    public static String convertLongDateToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static List<Statistic> removeDuplicatesInStatistics(List<Statistic> initialStatistics) {
        List<Statistic> newStatistics = new ArrayList<>();
        newStatistics.add(initialStatistics.get(0));
        if (initialStatistics.size() > 1) {
            for (int i = 1, j = 0; i < initialStatistics.size(); i++) {
                if (newStatistics.get(j).getName().equals(initialStatistics.get(i).getName())) {
                    newStatistics.get(j).setTotalCost(newStatistics.get(j).getTotalCost()
                            + initialStatistics.get(i).getTotalCost());
                } else {
                    newStatistics.add(initialStatistics.get(i));
                    j++;
                }
            }
        }
//        Log.d(TAG, "_________________________________________removeDuplicatesInStatistics: ");
//        Log.d(TAG, "iS = " + initialStatistics.size() + " | nS = " + newStatistics.size());
//        Log.d(TAG, "_________________________________________removeDuplicatesInStatistics: ");
        return newStatistics;
    }

    // FIXME: 24.10.2017
    public static double calculateTotalCostInStatistics(List<Statistic> statistics) {
        double totalCost = 0;
        for (Statistic s : statistics) {
            totalCost += s.getTotalCost();
        }
        return totalCost;
    }

}