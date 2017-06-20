package com.lidchanin.crudindiploma.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lidchanin.crudindiploma.data.MyCursorWrapper;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;

import java.util.ArrayList;
import java.util.List;

import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_IS_PURCHASED;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_LIST_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_PRODUCT_ID;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_QUANTITY_OR_WEIGHT;
import static com.lidchanin.crudindiploma.data.DatabaseHelper.TABLE_EXISTING_PRODUCTS;

/**
 * The class <code>ExistingProductDAO</code> extends {@link DatabaseDAO} and implements database
 * operations such as add, update, delete, get {@link ExistingProduct}.
 *
 * @author Lidchanin
 */
public class ExistingProductDAO extends DatabaseDAO {

    private static final String ID_EQUALS = COLUMN_ID + " =?";
    private static final String LIST_ID_EQUALS = COLUMN_LIST_ID + " =?";
    private static final String PRODUCT_ID_EQUALS = COLUMN_PRODUCT_ID + " =?";

    public ExistingProductDAO(Context context) {
        super(context);
    }

    /**
     * The method <code>getContentValues</code> fills the ContentValues with the
     * {@link ExistingProduct}.
     *
     * @param existingProduct is the product.
     * @return filled ContentValues.
     */
    private static ContentValues getContentValues(ExistingProduct existingProduct) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LIST_ID, existingProduct.getShoppingListId());
        contentValues.put(COLUMN_PRODUCT_ID, existingProduct.getProductId());
        contentValues.put(COLUMN_QUANTITY_OR_WEIGHT, existingProduct.getQuantityOrWeight());
        contentValues.put(COLUMN_IS_PURCHASED, existingProduct.isPurchased() ? 1 : 0);
        return contentValues;
    }

    /**
     * The method <code>queryExistingProducts</code> wraps the {@link Cursor}.
     *
     * @param columns       A list of which columns to return. Passing null will return all columns,
     *                      which is discouraged to prevent reading data from storage that isn't
     *                      going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an SQL WHERE
     *                      clause (excluding the WHERE itself). Passing null will return all rows
     *                      for the given table.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in order that they appear in the selection. The
     *                      values will be bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL GROUP BY
     *                      clause (excluding the GROUP BY itself). Passing null will cause the rows
     *                      to not be grouped.
     * @param having        A filter declare which row groups to include in the cursor, if row
     *                      grouping is being used, formatted as an SQL HAVING clause (excluding
     *                      the HAVING itself). Passing null will cause all row groups to be
     *                      included, and is required when row grouping is not being used.
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause (excluding
     *                      the ORDER BY itself). Passing null will use the default sort order,
     *                      which may be unordered.
     * @param limit         Limits the number of rows returned by the query, formatted as LIMIT
     *                      clause. Passing null denotes no LIMIT clause.
     * @return wrapped cursor.
     */
    private MyCursorWrapper queryExistingProducts(String[] columns, String selection,
                                                  String[] selectionArgs, String groupBy,
                                                  String having, String orderBy,
                                                  String limit) {
        Cursor cursor = database.query(TABLE_EXISTING_PRODUCTS, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        return new MyCursorWrapper(cursor);
    }

    /**
     * The method <code>getOne</code> gets existing product in the database.
     *
     * @param shoppingListId is the shopping list id, where product are located.
     * @param productId      is the product id, which you want to get.
     * @return existed product, which you need, or null.
     */
    public ExistingProduct getOne(long shoppingListId, long productId) {
        String[] columns = {COLUMN_ID, COLUMN_LIST_ID, COLUMN_PRODUCT_ID,
                COLUMN_QUANTITY_OR_WEIGHT, COLUMN_IS_PURCHASED};
        String selection = LIST_ID_EQUALS + " AND " + PRODUCT_ID_EQUALS;
        String[] selectionArgs = {String.valueOf(shoppingListId), String.valueOf(productId)};
        String limit = String.valueOf(1);
        MyCursorWrapper cursor = queryExistingProducts(columns, selection, selectionArgs,
                null, null, null, limit);
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getExistingProduct();
        } finally {
            cursor.close();
        }
    }

    /**
     * The method <code>getAllFromCurrentShoppingList</code> gets all existing products in needed
     * shopping list from the database.
     *
     * @param shoppingListId is the shopping list id, which contains needed products.
     * @return all existing products in shopping list, which you need, or null.
     */
    public List<ExistingProduct> getAllFromCurrentShoppingList(long shoppingListId) {
        List<ExistingProduct> existingProducts = new ArrayList<>();
        String[] columns = {COLUMN_ID, COLUMN_LIST_ID, COLUMN_PRODUCT_ID,
                COLUMN_QUANTITY_OR_WEIGHT, COLUMN_IS_PURCHASED};
        String selection = LIST_ID_EQUALS;
        String[] selectionArgs = {String.valueOf(shoppingListId)};
        database.beginTransaction();
        MyCursorWrapper cursor = queryExistingProducts(columns, selection, selectionArgs,
                null, null, null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                existingProducts.add(cursor.getExistingProduct());
                cursor.moveToNext();
//                database.yieldIfContendedSafely();
            }
            database.setTransactionSuccessful();
        } finally {
            cursor.close();
            database.endTransaction();
        }
        return existingProducts;
    }

    /**
     * The method <code>update</code> update existing product in the database.
     *
     * @param existingProduct is the existing product, which you need to update.
     * @return the number of rows affected.
     */
    public int update(ExistingProduct existingProduct) {
        ContentValues contentValues = getContentValues(existingProduct);
        return database.update(TABLE_EXISTING_PRODUCTS, contentValues, ID_EQUALS,
                new String[]{String.valueOf(existingProduct.getId())});
    }
}
