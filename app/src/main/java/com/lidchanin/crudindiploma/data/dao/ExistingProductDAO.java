package com.lidchanin.crudindiploma.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lidchanin.crudindiploma.data.DatabaseHelper;
import com.lidchanin.crudindiploma.data.models.ExistingProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Class <code>ExistingProductDAO</code> extends {@link DatabaseDAO} and implements database
 * operations such as add, update, delete, get, ...
 * {@link com.lidchanin.crudindiploma.data.models.ExistingProduct}.
 *
 * @author Lidchanin
 */
public class ExistingProductDAO extends DatabaseDAO {

    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID + " =?";

    public ExistingProductDAO(Context context) {
        super(context);
    }

    // FIXME: 15.05.2017 only test
    public List<String[]> getAllExistingProducts() {
        List<String[]> relationships = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_EXISTING_PRODUCTS,
                null, null, null, null, null, null);
        if ((cursor.moveToFirst())) {
            do {
                String[] relationship = new String[5];
                relationship[0] = String.valueOf(cursor.getLong(0));
                relationship[1] = String.valueOf(cursor.getLong(1));
                relationship[2] = String.valueOf(cursor.getLong(2));
                relationship[3] = String.valueOf(cursor.getDouble(3));
                relationship[4] = String.valueOf(cursor.getDouble(4));
                relationships.add(relationship);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return relationships;
    }

    /**
     * Method <code>getOne</code> gets existing product in the database.
     *
     * @param shoppingListId is the shopping list id, where product are located.
     * @param productId      is the product id, which you want to get.
     * @return existed product, which you need, or null.
     */
    public ExistingProduct getOne(long shoppingListId, long productId) {
        String selectQuery
                = "SELECT " + DatabaseHelper.COLUMN_ID + ", "
                + DatabaseHelper.COLUMN_LIST_ID + ", " + DatabaseHelper.COLUMN_PRODUCT_ID + ", "
                + DatabaseHelper.COLUMN_QUANTITY_OR_WEIGHT + ", "
                + DatabaseHelper.COLUMN_TOTAL_COST
                + " FROM " + DatabaseHelper.TABLE_EXISTING_PRODUCTS
                + " WHERE " + DatabaseHelper.COLUMN_LIST_ID + " ='" + shoppingListId + "'"
                + " AND " + DatabaseHelper.COLUMN_PRODUCT_ID + " ='" + productId + "'"
                + " LIMIT 1";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            ExistingProduct existingProduct = new ExistingProduct();
            existingProduct.setId(cursor.getLong(0));
            existingProduct.setShoppingListId(cursor.getLong(1));
            existingProduct.setProductId(cursor.getLong(2));
            existingProduct.setQuantityOrWeight(cursor.getDouble(3));
            existingProduct.setTotalCost(cursor.getDouble(4));
            cursor.close();
            return existingProduct;
        } else {
            cursor.close();
            return new ExistingProduct();
        }
    }

    /**
     * Method <code>getAllFromCurrentShoppingList</code> gets all existing products in needed
     * shopping list from the database.
     *
     * @param shoppingListId is the shopping list id, which contains needed products.
     * @return all existing products in shopping list, which you need, or null.
     */
    public List<ExistingProduct> getAllFromCurrentShoppingList(long shoppingListId) {
        List<ExistingProduct> existingProducts = new ArrayList<>();
        String selectQuery
                = "SELECT " + DatabaseHelper.COLUMN_ID + ", "
                + DatabaseHelper.COLUMN_LIST_ID + ", " + DatabaseHelper.COLUMN_PRODUCT_ID + ", "
                + DatabaseHelper.COLUMN_QUANTITY_OR_WEIGHT + ", "
                + DatabaseHelper.COLUMN_TOTAL_COST
                + " FROM " + DatabaseHelper.TABLE_EXISTING_PRODUCTS
                + " WHERE " + DatabaseHelper.COLUMN_LIST_ID + " = '" + shoppingListId + "'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ExistingProduct existingProduct = new ExistingProduct();
                existingProduct.setId(cursor.getLong(0));
                existingProduct.setShoppingListId(cursor.getLong(1));
                existingProduct.setProductId(cursor.getLong(2));
                existingProduct.setQuantityOrWeight(cursor.getDouble(3));
                existingProduct.setTotalCost(cursor.getDouble(4));
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
     * Method <code>update</code> update existing product in the database.
     *
     * @param existingProduct is the existing product, which you need to update.
     * @return the number of rows affected.
     */
    public int update(ExistingProduct existingProduct) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_ID, existingProduct.getId());
        contentValues.put(DatabaseHelper.COLUMN_LIST_ID, existingProduct.getShoppingListId());
        contentValues.put(DatabaseHelper.COLUMN_PRODUCT_ID, existingProduct.getProductId());
        contentValues.put(DatabaseHelper.COLUMN_QUANTITY_OR_WEIGHT,
                existingProduct.getQuantityOrWeight());
        contentValues.put(DatabaseHelper.COLUMN_TOTAL_COST, existingProduct.getTotalCost());
        return database.update(DatabaseHelper.TABLE_EXISTING_PRODUCTS, contentValues,
                WHERE_ID_EQUALS, new String[]{String.valueOf(existingProduct.getId())});
    }
}
