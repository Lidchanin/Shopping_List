package com.lidchanin.crudindiploma.utils;

import com.lidchanin.crudindiploma.database.Product;
import com.lidchanin.crudindiploma.database.ShoppingList;
import com.lidchanin.crudindiploma.database.UsedProduct;

import java.util.List;

public class DatabaseUtils {

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
     * The method <b>calculationOfEstimatedAmount</b> calculates of estimated amounts all costs
     * {@link UsedProduct}s in {@link ShoppingList}.
     *
     * @return estimated amount of all {@link UsedProduct}s in {@link ShoppingList}.
     */
    public static double calculationOfEstimatedAmount(List<UsedProduct> usedProducts) {
        double estimatedAmount = 0;
        for (UsedProduct up : usedProducts) {
            estimatedAmount += up.getQuantity() * up.getProduct().getCost();
        }
        return estimatedAmount;
    }
}
