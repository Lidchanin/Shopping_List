package com.lidchanin.crudindiploma.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lidchanin.crudindiploma.data.ShoppingListCursorWrapper;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;

import java.util.ArrayList;
import java.util.List;

import static com.lidchanin.crudindiploma.data.DatabaseHelper.COLUMN_ID;
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

    private static final String WHERE_ID_EQUALS = COLUMN_ID + " =?";

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
        return contentValues;
    }

    /**
     * The method <code>queryShoppingLists</code> wraps the {@link Cursor}.
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
    private ShoppingListCursorWrapper queryProducts(String[] columns, String selection,
                                                    String[] selectionArgs, String groupBy,
                                                    String having, String orderBy,
                                                    String limit) {
        Cursor cursor = database.query(TABLE_EXISTING_PRODUCTS, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        return new ShoppingListCursorWrapper(cursor);
    }

    /**
     * The method <code>getOne</code> gets existing product in the database.
     *
     * @param shoppingListId is the shopping list id, where product are located.
     * @param productId      is the product id, which you want to get.
     * @return existed product, which you need, or null.
     */
    public ExistingProduct getOne(long shoppingListId, long productId) {
        String selectQuery
                = "SELECT " + COLUMN_ID + ", " + COLUMN_LIST_ID + ", " + COLUMN_PRODUCT_ID + ", "
                + COLUMN_QUANTITY_OR_WEIGHT
                + " FROM " + TABLE_EXISTING_PRODUCTS
                + " WHERE " + COLUMN_LIST_ID + " ='" + shoppingListId + "'"
                + " AND " + COLUMN_PRODUCT_ID + " ='" + productId + "'"
                + " LIMIT 1";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            ExistingProduct existingProduct = new ExistingProduct();
            existingProduct.setId(cursor.getLong(0));
            existingProduct.setShoppingListId(cursor.getLong(1));
            existingProduct.setProductId(cursor.getLong(2));
            existingProduct.setQuantityOrWeight(cursor.getDouble(3));
            cursor.close();
            return existingProduct;
        } else {
            cursor.close();
            return new ExistingProduct();
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
        String selectQuery
                = "SELECT " + COLUMN_ID + ", " + COLUMN_LIST_ID + ", " + COLUMN_PRODUCT_ID + ", "
                + COLUMN_QUANTITY_OR_WEIGHT
                + " FROM " + TABLE_EXISTING_PRODUCTS
                + " WHERE " + COLUMN_LIST_ID + " = '" + shoppingListId + "'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ExistingProduct existingProduct = new ExistingProduct();
                existingProduct.setId(cursor.getLong(0));
                existingProduct.setShoppingListId(cursor.getLong(1));
                existingProduct.setProductId(cursor.getLong(2));
                existingProduct.setQuantityOrWeight(cursor.getDouble(3));
                existingProducts.add(existingProduct);
            } while (cursor.moveToNext());
            cursor.close();
            return existingProducts;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * The method <code>update</code> update existing product in the database.
     *
     * @param existingProduct is the existing product, which you need to update.
     * @return the number of rows affected.
     */
    public int update(ExistingProduct existingProduct) {
        ContentValues contentValues = getContentValues(existingProduct);
        return database.update(TABLE_EXISTING_PRODUCTS, contentValues, WHERE_ID_EQUALS,
                new String[]{String.valueOf(existingProduct.getId())});
    }
}
