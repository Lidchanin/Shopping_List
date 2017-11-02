package com.lidchanin.crudindiploma.utils;

import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.ShoppingList;
import com.lidchanin.crudindiploma.database.Statistic;
import com.lidchanin.crudindiploma.database.UsedProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
     * The method <b>divideStatisticsByMonths</b> divides the list with {@link Statistic}s into
     * the categories. "One" date - "many" statistics, which corresponds by date.
     *
     * @param initialStatistics all initial {@link Statistic}s in {@link List}.
     * @return divided {@link List} with statistics {@link List}s, which are sorted by months.
     */
    public static List<List<Statistic>> divideStatisticsByMonths(List<Statistic> initialStatistics) {
        List<List<Statistic>> dividedStatistics = new ArrayList<>();
        if (initialStatistics != null && initialStatistics.size() > 0) {
            long currentMonth = getCurrentMonth(initialStatistics.get(0).getDate());
            List<Statistic> statisticsOneMonth = new ArrayList<>();
            statisticsOneMonth.add(initialStatistics.get(0));
            for (int i = 1; i < initialStatistics.size(); i++) {
                if (getCurrentMonth(initialStatistics.get(i).getDate()) == currentMonth) {
                    statisticsOneMonth.add(initialStatistics.get(i));
                } else {
                    dividedStatistics.add(statisticsOneMonth);
                    statisticsOneMonth = new ArrayList<>();
                    statisticsOneMonth.add(initialStatistics.get(i));
                    currentMonth = getCurrentMonth(initialStatistics.get(i).getDate());
                }
            }
            dividedStatistics.add(statisticsOneMonth);
        }
        return dividedStatistics;
    }

    /**
     * The method <b>getCurrentMonth</b> convert date with days, hours and etc in milliseconds to
     * date with month in milliseconds.
     *
     * @param dateInMillis initial date.
     * @return date with only month in millis.
     */
    public static long getCurrentMonth(long dateInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.clear(Calendar.AM_PM);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTimeInMillis();
    }

    /**
     * The method <b>getMonthWithStep</b> The method changes the current month to the needed.
     *
     * @param currentMonth the current month in millis.
     * @param steps        the number of steps.
     * @return the needed month in millis.
     */
    public static long getMonthWithStep(long currentMonth, int steps) {
        if (steps == 0) {
            return getCurrentMonth(currentMonth);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentMonth);
            calendar.add(Calendar.MONTH, steps);
            return calendar.getTime().getTime();
        }
    }

    /**
     * The method <b>convertDateInMillisToString</b> convert date in milliseconds to date in String.
     *
     * @param millis the date in milliseconds.
     * @return date in {@link String} format.
     */
    public static String convertDateInMillisToString(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return sdf.format(millis);
    }

    /**
     * The method <b>removeDuplicatesInStatistics</b> finds duplicates by name in list.
     * Then duplicates are combined.
     *
     * @param initialStatistics statistics with duplicates.
     * @return statistics without duplicates.
     */
    public static List<Statistic> removeDuplicatesInStatistics(List<Statistic> initialStatistics) {
        sortListStatisticsByName(initialStatistics);
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
     * The method <b>sortListStatisticsByName</b> sorts statistics list by name by asc.
     *
     * @param statistics initial statistics list.
     */
    private static void sortListStatisticsByName(List<Statistic> statistics) {
        if (statistics.size() > 0) {
            Collections.sort(statistics, new Comparator<Statistic>() {
                @Override
                public int compare(final Statistic object1, final Statistic object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
    }

}