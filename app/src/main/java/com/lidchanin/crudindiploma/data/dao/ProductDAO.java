package com.lidchanin.crudindiploma.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.lidchanin.crudindiploma.data.DatabaseHelper;
import com.lidchanin.crudindiploma.data.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Class <code>ProductDAO</code> extends {@link DatabaseDAO} and implements database operations
 * such as add, update, delete, get, ...
 * {@link com.lidchanin.crudindiploma.data.models.Product}.
 *
 * @author Lidchanin
 */
public class ProductDAO extends DatabaseDAO {

    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID + " =?";
    private static final String WHERE_NAME_EQUALS = DatabaseHelper.COLUMN_NAME + " =?";
    private static final String WHERE_LIST_ID_EQUALS = DatabaseHelper.COLUMN_LIST_ID + " =?";
    private static final String WHERE_PRODUCT_ID_EQUALS = DatabaseHelper.COLUMN_PRODUCT_ID + " =?";

    public ProductDAO(Context context) {
        super(context);
    }

    /**
     * Method <code>getOneById</code> gets product by id in the database.
     *
     * @param productId is the product id, which you want to get.
     * @return product, which you need, or null.
     */
    public Product getOneById(long productId) {
        String[] columns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_COST, DatabaseHelper.COLUMN_POPULARITY};
        String[] selectionArgs = {String.valueOf(productId)};
        String limit = String.valueOf(1);
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCTS, columns,
                WHERE_ID_EQUALS, selectionArgs, null, null, null, limit);
        if (cursor.moveToFirst()) {
            Product product = new Product();
            product.setId(cursor.getLong(0));
            product.setName(cursor.getString(1));
            product.setCost(cursor.getDouble(2));
            product.setPopularity(cursor.getLong(3));
            cursor.close();
            return product;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * Method <code>getOneByName</code> gets product by name in the database.
     *
     * @param productName is the product name, which you want to get.
     * @return product, which you need, or null.
     */
    public Product getOneByName(String productName) {
        String[] columns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_COST, DatabaseHelper.COLUMN_POPULARITY};
        String[] selectionArgs = {productName};
        String limit = String.valueOf(1);
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCTS, columns, WHERE_NAME_EQUALS,
                selectionArgs, null, null, null, limit);
        if (cursor.moveToFirst()) {
            Product product = new Product();
            product.setId(cursor.getLong(0));
            product.setName(cursor.getString(1));
            product.setCost(cursor.getDouble(2));
            product.setPopularity(cursor.getLong(3));
            cursor.close();
            return product;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * Method <code>getAll</code> gets all products from the database.
     *
     * @return all products, which you need.
     */
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String[] columns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_COST, DatabaseHelper.COLUMN_POPULARITY};
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCTS, columns, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            Product product = new Product();
            product.setId(cursor.getLong(0));
            product.setName(cursor.getString(1));
            product.setCost(cursor.getDouble(2));
            product.setPopularity(cursor.getLong(3));
            products.add(product);
        }
        cursor.close();
        return products;
    }

    /**
     * Method <code>getAllFromCurrentShoppingList</code> gets all products in needed
     * shopping list from the database.
     *
     * @param shoppingListId is the shopping list id, which contains needed products.
     * @return all products in shopping list, which you need, or null.
     */
    public List<Product> getAllFromCurrentShoppingList(long shoppingListId) {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PRODUCTS + " tp, "
                + DatabaseHelper.TABLE_SHOPPING_LISTS + " tl, "
                + DatabaseHelper.TABLE_EXISTING_PRODUCTS + " tep "
                + "WHERE tl." + DatabaseHelper.COLUMN_ID + " = '" + shoppingListId + "'"
                + " AND " + "tp." + DatabaseHelper.COLUMN_ID
                + " = " + "tep." + DatabaseHelper.COLUMN_PRODUCT_ID
                + " AND " + "tl." + DatabaseHelper.COLUMN_ID
                + " = " + "tep." + DatabaseHelper.COLUMN_LIST_ID;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getLong(0));
                product.setName(cursor.getString(1));
                product.setCost(cursor.getDouble(2));
                product.setPopularity(cursor.getLong(3));
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
            return products;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * Method <code>getTopFiveProducts</code> gets five products that are most popular from the
     * database without already existed products.
     *
     * @param existedProducts are products that exist in the shopping list.
     * @return five products or null.
     */
    public List<Product> getTopFiveProducts(List<Product> existedProducts) {
        List<Product> products = new ArrayList<>();
        String[] existedProductsIdsArray = new String[existedProducts.size()];
        for (int i = 0; i < existedProductsIdsArray.length; i++) {
            existedProductsIdsArray[i] = String.valueOf(existedProducts.get(i).getId());
        }
        String existedProductsIds = TextUtils.join(", ", existedProductsIdsArray);
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PRODUCTS
                + " WHERE " + DatabaseHelper.COLUMN_ID + "  NOT IN (" + existedProductsIds + ")"
                + " ORDER BY " + DatabaseHelper.COLUMN_POPULARITY + " DESC"
                + " LIMIT 5";
        Cursor cursor = database.rawQuery(selectQuery, new String[0]);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getLong(0));
                product.setName(cursor.getString(1));
                product.setCost(cursor.getDouble(2));
                product.setPopularity(cursor.getLong(3));
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
            return products;
        } else {
            cursor.close();
            return null;
        }
    }

    /**
     * Method <code>addInCurrentShoppingList</code> adds product in the database and assign
     * it to shopping list.
     *
     * @param product        is the product, which you want to add to the database.
     * @param shoppingListId is the shopping list id.
     * @return added or updated product id.
     * @see #assignProductToShoppingList(long, long)
     * @see #isExistRelationship(long, long)
     */
    public long addInCurrentShoppingList(Product product, long shoppingListId) {
        long productId;
        Product existedProduct = getOneByName(product.getName());
        if (existedProduct == null) {
            Log.i("MY_LOG " + ProductDAO.class.getSimpleName(), "Product is not exist.");
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.COLUMN_NAME, product.getName());
            contentValues.put(DatabaseHelper.COLUMN_COST, product.getCost());
            contentValues.put(DatabaseHelper.COLUMN_POPULARITY, product.getPopularity());
            productId = database.insert(DatabaseHelper.TABLE_PRODUCTS, null, contentValues);
            assignProductToShoppingList(shoppingListId, productId);
        } else {
            Log.i("MY_LOG " + ProductDAO.class.getSimpleName(), "Product is already exist.");
            product.setId(existedProduct.getId());
            product.setPopularity(existedProduct.getPopularity() + 1);
            productId = product.getId();
            update(product);
            if (!isExistRelationship(shoppingListId, productId)) {
                assignProductToShoppingList(shoppingListId, productId);
            }
        }
        return productId;
    }

    /**
     * Method <code>update</code> update product in the database.
     *
     * @param product is the product, which you need to update.
     * @return the number of rows affected.
     */
    public long update(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, product.getName());
        contentValues.put(DatabaseHelper.COLUMN_COST, product.getCost());
        contentValues.put(DatabaseHelper.COLUMN_POPULARITY, product.getPopularity());
        return database.update(DatabaseHelper.TABLE_PRODUCTS, contentValues,
                WHERE_ID_EQUALS, new String[]{String.valueOf(product.getId())});
    }

    /**
     * Method <code>deleteInDatabase</code> delete product in the database.
     *
     * @param productId is the product id, which you want to deleteInDatabase.
     */
    public void deleteInDatabase(long productId) {
        database.delete(DatabaseHelper.TABLE_PRODUCTS, WHERE_ID_EQUALS,
                new String[]{String.valueOf(productId)});
    }

    /**
     * Method <code>delete</code> delete product only in shopping list.
     *
     * @param shoppingListId is the current shopping list id, which contains needed product.
     * @param productId      is the product id, which you want to delete form shopping list.
     */
    public void delete(long shoppingListId, long productId) {
        database.delete(DatabaseHelper.TABLE_EXISTING_PRODUCTS,
                WHERE_LIST_ID_EQUALS + " AND " + WHERE_PRODUCT_ID_EQUALS,
                new String[]{String.valueOf(shoppingListId), String.valueOf(productId)});
    }

    /**
     * Method <code>assignProductToShoppingList</code> assign product to shopping list.
     *
     * @param shoppingListId is the shopping list id.
     * @param productId      is the product id, which you want to assign to shopping list.
     */
    public void assignProductToShoppingList(long shoppingListId, long productId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_LIST_ID, shoppingListId);
        contentValues.put(DatabaseHelper.COLUMN_PRODUCT_ID, productId);
        database.insert(DatabaseHelper.TABLE_EXISTING_PRODUCTS, null, contentValues);
    }

    /**
     * Method <code>isExistRelationship</code> checks the relationship exists or not.
     *
     * @param shoppingListId is the shopping list id.
     * @param productId      is the product id.
     * @return there is relationship or not.
     */
    private boolean isExistRelationship(long shoppingListId, long productId) {
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_EXISTING_PRODUCTS
                + " WHERE "
                + DatabaseHelper.COLUMN_LIST_ID + " = " + shoppingListId
                + " AND "
                + DatabaseHelper.COLUMN_PRODUCT_ID + " = " + productId;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
}