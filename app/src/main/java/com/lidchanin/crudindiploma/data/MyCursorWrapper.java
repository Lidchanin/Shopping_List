package com.lidchanin.crudindiploma.data;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.lidchanin.crudindiploma.data.models.ExistingProduct;
import com.lidchanin.crudindiploma.data.models.Product;
import com.lidchanin.crudindiploma.data.models.ShoppingList;

import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_COST;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_DATE_OF_CREATION;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_IS_PURCHASED;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_LIST_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_NAME;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_POPULARITY;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_PRODUCT_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_QUANTITY_OR_WEIGHT;

/**
 * The class <code>MyCursorWrapper</code> extends {@link CursorWrapper}. This class
 * creates wrapper for {@link Cursor}.
 *
 * @author Lidchanin
 */
public class MyCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public MyCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * The method <code>getShoppingListWithoutDate</code> needs for filling {@link ShoppingList}
     * without date of creation.
     *
     * @return filled shopping list.
     */
    public ShoppingList getShoppingListWithoutDate() {
        long id = getLong(getColumnIndex(COLUMN_ID));
        String name = getString(getColumnIndex(COLUMN_NAME));

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(id);
        shoppingList.setName(name);

        return shoppingList;
    }

    /**
     * The method <code>getShoppingListWithoutDate</code> needs for filling {@link ShoppingList}
     * without date of creation.
     *
     * @return filled shopping list.
     */
    public ShoppingList getShoppingListWithDate() {
        long id = getLong(getColumnIndex(COLUMN_ID));
        String name = getString(getColumnIndex(COLUMN_NAME));
        String dateOfCreation = getString(getColumnIndex(COLUMN_DATE_OF_CREATION));

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(id);
        shoppingList.setName(name);
        shoppingList.setDateOfCreation(dateOfCreation);

        return shoppingList;
    }

    /**
     * The method <code>getProduct</code> needs for filling {@link Product}.
     *
     * @return filled product.
     */
    public Product getProduct() {
        long id = getLong(getColumnIndex(COLUMN_ID));
        String name = getString(getColumnIndex(COLUMN_NAME));
        double cost = getDouble(getColumnIndex(COLUMN_COST));
        long popularity = getLong(getColumnIndex(COLUMN_POPULARITY));

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCost(cost);
        product.setPopularity(popularity);

        return product;
    }

    /**
     * The method <code>getExistingProduct</code> needs for filling {@link ExistingProduct}.
     *
     * @return filled existing product.
     */
    public ExistingProduct getExistingProduct() {
        long id = getLong(getColumnIndex(COLUMN_ID));
        long listId = getLong(getColumnIndex(COLUMN_LIST_ID));
        long productId = getLong(getColumnIndex(COLUMN_PRODUCT_ID));
        double quantity = getDouble(getColumnIndex(COLUMN_QUANTITY_OR_WEIGHT));
        boolean isPurchased = getInt(getColumnIndex(COLUMN_IS_PURCHASED)) == 1;

        ExistingProduct existingProduct = new ExistingProduct();
        existingProduct.setId(id);
        existingProduct.setShoppingListId(listId);
        existingProduct.setProductId(productId);
        existingProduct.setQuantityOrWeight(quantity);
        existingProduct.setPurchased(isPurchased);

        return existingProduct;
    }
}
