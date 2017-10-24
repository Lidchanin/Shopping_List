package com.lidchanin.crudindiploma.utils;

import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.ShoppingList;
import com.lidchanin.crudindiploma.database.Statistic;
import com.lidchanin.crudindiploma.database.UsedProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * @author Lidchanin
 */
public class ModelUtils {

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
     * The method <b>divideStatisticsByMonths</b> divides the list with {@link Statistic}s into
     * the categories. "One" date - "many" statistics, which corresponds by date.
     *
     * @param initialStatistics all started {@link Statistic}s in list.
     * @return divided list with statistics.
     */
    public static List<List<Statistic>> divideStatisticsByMonths(List<Statistic> initialStatistics) {
        List<List<Statistic>> sortedStatistics = new ArrayList<>();
        if (initialStatistics != null && initialStatistics.size() > 0) {
            String month = convertLongDateToString(initialStatistics.get(0).getDate());
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

    /**
     * The method <b>removeDuplicatesInStatistics</b> finds duplicates in list. Then duplicates
     * are combined.
     *
     * @param initialStatistics statistics with duplicates.
     * @return statistics without duplicates.
     */
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
        return newStatistics;
    }

    /**
     * The method <b>calculateTotalCostInStatistics</b> calculates total cost in all statistics.
     *
     * @param statistics the {@link List} with statistics.
     * @return total cost.
     */
    public static double calculateTotalCostInStatistics(List<Statistic> statistics) {
        double totalCost = 0;
        for (Statistic s : statistics) {
            totalCost += s.getTotalCost();
        }
        return totalCost;
    }

    /**
     * The method <b>sortStatisticsByName</b> sorts statistics in lists by name by asc.
     *
     * @param statistics initial statistics list.
     */
    public static void sortStatisticsByName(List<List<Statistic>> statistics) {
        for (List<Statistic> sl : statistics) {
            if (sl.size() > 0) {
                Collections.sort(sl, new Comparator<Statistic>() {
                    @Override
                    public int compare(final Statistic object1, final Statistic object2) {
                        return object1.getName().compareTo(object2.getName());
                    }
                });
            }
        }
    }

}